package com.ietswise.service.impl;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Attendee;
import biweekly.property.Method;
import biweekly.property.Organizer;
import biweekly.util.Duration;
import com.ietswise.entity.BookingSessionData;
import com.ietswise.service.CalendarMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

import static biweekly.Biweekly.write;
import static biweekly.parameter.ParticipationLevel.REQUIRED;
import static java.util.Date.from;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.Session.getInstance;
import static javax.mail.Transport.send;

@Service
public class CalendarMailServiceImpl implements CalendarMailService {

    @Value("${ieltswise67.lesson.trail.long}")
    private Integer trailSession;
    @Value("${google.mail.info.ieltswise67.email}")
    private String infoEmail;
    @Value("${google.mail.info.ieltswise67.code}")
    private String infoCode;

    public boolean bookFreeTrailLesson(final BookingSessionData sessionData) {
        try {
            final Properties properties = prepareProperties();
            final Authenticator auth = prepareAuthenticator(infoEmail, infoCode);
            final Session mailSession = getInstance(properties, auth);
            final MimeMessage message = createNotificationMessage(mailSession, sessionData);
            send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Properties prepareProperties() {
        final Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return properties;
    }

    private Authenticator prepareAuthenticator(final String tutorEmail, final String tutorCode) {
        return new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(tutorEmail, tutorCode);
            }
        };
    }

    private MimeMessage createNotificationMessage(final Session mailSession, BookingSessionData sessionData) throws Exception {
        final MimeMessage message = new MimeMessage(mailSession);
        final MimeMultipart mixedMultipart = new MimeMultipart("mixed");
        mixedMultipart.addBodyPart(createICalPart(new MimeBodyPart(), sessionData));
        message.setContent(mixedMultipart);
        message.setSubject("Free trail lesson with IELTS Wise67");

        final InternetAddress studentAddress = new InternetAddress(sessionData.getStudentEmail(), "Student");
        final InternetAddress tutorAddress = new InternetAddress(sessionData.getTutorEmail(), "Tutor");
        final InternetAddress fromAddress = new InternetAddress(infoEmail, "IELTSWise Platform");
        message.setRecipients(TO, new Address[]{studentAddress, tutorAddress});
        message.setSender(fromAddress);
        return message;
    }

    private <T extends MimePart> T createICalPart(T mimePartForCalendar, BookingSessionData sessionData) throws Exception {
        final DataSource source = new ByteArrayDataSource(displayInfoInCalendar(sessionData), "text/calendar; charset=UTF-8");
        mimePartForCalendar.setDataHandler(new DataHandler(source));
        mimePartForCalendar.setHeader("Content-Type", "text/calendar; charset=UTF-8; method=REQUEST");
        return mimePartForCalendar;
    }

    private String displayInfoInCalendar(BookingSessionData sessionData) {
        ICalendar calendar = new ICalendar();
        calendar.addProperty(new Method(Method.REQUEST));

        final VEvent event = new VEvent();
        event.setSummary("Free trail lesson with IELTS Wise67");
        event.setDateStart(from(sessionData.getStartDate().toInstant()));
        event.setDuration(new Duration.Builder()
                .minutes(trailSession)
                .build());
        event.setOrganizer(new Organizer("Tutor", infoEmail));

        Attendee tutor = new Attendee("Tutor", sessionData.getTutorEmail());
        tutor.setParticipationLevel(REQUIRED);
        Attendee student = new Attendee("Student", sessionData.getStudentEmail());
        student.setParticipationLevel(REQUIRED);
        event.addAttendee(student);
        event.addAttendee(tutor);
        calendar.addEvent(event);

        return write(calendar).go();
    }
}
