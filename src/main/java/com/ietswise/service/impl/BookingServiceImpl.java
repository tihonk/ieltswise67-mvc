package com.ietswise.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.ietswise.entity.BookingSessionData;
import com.ietswise.entity.UsedTrialLessons;
import com.ietswise.repository.TrialSessions;
import com.ietswise.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.load;
import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;
import static com.google.api.client.json.gson.GsonFactory.getDefaultInstance;
import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Service
public class BookingServiceImpl implements BookingService {

    private static final JsonFactory JSON_FACTORY = getDefaultInstance();
    private static final List<String> SCOPES = singletonList(CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "src/main/resources/tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final TrialSessions trialSessionsRepository;

    @Autowired
    public BookingServiceImpl(TrialSessions trialSessionsRepository) {
        this.trialSessionsRepository = trialSessionsRepository;
    }

    private String bookSession(final BookingSessionData sessionData) {
        // TODO: Add logs about event creation
        try {
            final Event event = prepareAndSendEvent(sessionData);
            return event.getHtmlLink();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String bookTrialSession(BookingSessionData sessionData) {
        String studentEmail = sessionData.getStudentEmail();
        if (isUsedTrialLessonByStudent(studentEmail)) {
            throw new RuntimeException("User with ID " + studentEmail + " has already used a trial lesson");
        } else {
            String htmlLink = bookSession(sessionData);
            if (htmlLink != null) {
                saveStudentUsedTrialLesson(studentEmail);
            }
            return htmlLink;
        }
    }

    @Override
    public String bookRegularSession(BookingSessionData sessionData) {
        return bookSession(sessionData);
    }

    private boolean isUsedTrialLessonByStudent(String studentEmail) {
        UsedTrialLessons student = trialSessionsRepository.findByEmail(studentEmail);
        return student != null;
    }

    private void saveStudentUsedTrialLesson(String email) {
        UsedTrialLessons student = new UsedTrialLessons();
        student.setEmail(email);
        student.setCreated(LocalDateTime.now());
        trialSessionsRepository.save(student);
    }

    private Event prepareAndSendEvent(final BookingSessionData sessionData) throws GeneralSecurityException, IOException {
        final Event event = buildEventWithMeet(sessionData);
        final NetHttpTransport httpTransport = newTrustedTransport();
        final Calendar calendar = buildCalendarService(httpTransport);

        return calendar.events()
                .insert("primary", event)
                .setConferenceDataVersion(1)
                .setSendUpdates("all")
                .execute();
    }

    private Event buildEventWithMeet(BookingSessionData sessionData) {
        // TODO: Add the ability to book regular sessions by using ".setRecurrence"
        return new Event()
                .setSummary("English with IELTSWise67")
                .setLocation("Online")
                .setDescription("A chance to learn English with IELTSWise67 and discover new opportunities.")
                .setConferenceData(prepareConferenceData())
                .setStart(prepareEventTime(sessionData.getStartDate()))
                .setEnd(prepareEventTime(sessionData.getEndDate()))
                .setAttendees(prepareEventAttendees(sessionData))
                .setGuestsCanModify(true)
                .setReminders(prepareReminders());
    }

    private ConferenceData prepareConferenceData() {
        final ConferenceSolutionKey conferenceSKey = createConferenceKey();
        final CreateConferenceRequest conferenceReq = createConferenceRequest(conferenceSKey);
        return createConferenceData(conferenceReq);
    }

    private EventDateTime prepareEventTime(String eventTime) {
        final DateTime dateTime = new DateTime(eventTime);
        return new EventDateTime()
                .setDateTime(dateTime)
                .setTimeZone("Europe/London");
    }

    private List<EventAttendee> prepareEventAttendees(final BookingSessionData sessionData) {
        final EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(sessionData.getStudentEmail()),
                new EventAttendee().setEmail(sessionData.getTutorEmail()).setResource(true).setOrganizer(true),
        };
        return asList(attendees);
    }

    private Event.Reminders prepareReminders() {
        final EventReminder[] reminderOverrides = createReminderOverrides();
        return new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(asList(reminderOverrides));
    }

    private ConferenceSolutionKey createConferenceKey() {
        final ConferenceSolutionKey conferenceSKey = new ConferenceSolutionKey();
        conferenceSKey.setType("hangoutsMeet");
        return conferenceSKey;
    }

    private CreateConferenceRequest createConferenceRequest(ConferenceSolutionKey conferenceSKey) {
        final CreateConferenceRequest createConferenceReq = new CreateConferenceRequest();
        createConferenceReq.setRequestId("RequestId");
        createConferenceReq.setConferenceSolutionKey(conferenceSKey);
        return createConferenceReq;
    }

    private ConferenceData createConferenceData(CreateConferenceRequest conferenceReq) {
        final ConferenceData conferenceData = new ConferenceData();
        conferenceData.setCreateRequest(conferenceReq);
        return conferenceData;
    }

    private EventReminder[] createReminderOverrides() {
        return new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
    }

    private Calendar buildCalendarService(NetHttpTransport httpTransport) throws IOException {
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName("IELTSWise Google API")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        final InputStream in = BookingServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        final GoogleClientSecrets clientSecrets = load(JSON_FACTORY, new InputStreamReader(in));

        final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
        final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
