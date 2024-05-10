package com.ieltswise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltswise.controller.request.StudentCommentRequest;
import com.ieltswise.entity.StudentComment;
import com.ieltswise.exception.EmailNotFoundException;
import com.ieltswise.exception.NoPurchasedLessonsException;
import com.ieltswise.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    StudentComment studentComment;

    @BeforeEach
    void setUp() {
        studentComment = StudentComment.builder()
                .id(1L)
                .name("Bob")
                .email("Bob001@gmail.com")
                .value("I like learning English.")
                .created(LocalDateTime.of(2024, 5, 2, 20, 19, 35, 0))
                .build();
    }

    @Test
    public void testGetAllCommentsReturnAllComments() throws Exception {

        // Given
        List<StudentComment> comments = Collections.singletonList(studentComment);

        // When
        when(commentService.getAllComments()).thenReturn(comments);

        // Then
        mockMvc.perform(get("/comments"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(comments.size())))
                .andExpect(jsonPath("$[0].id").value(studentComment.getId()))
                .andExpect(jsonPath("$[0].name").value(studentComment.getName()))
                .andExpect(jsonPath("$[0].email").value(studentComment.getEmail()))
                .andExpect(jsonPath("$[0].value").value(studentComment.getValue()))
                .andExpect(jsonPath("$[0].created").value(studentComment.getCreated().toString()))
                .andExpect(status().isOk());
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void testCreateCommentReturnCreated() throws Exception {

        // Given
        StudentCommentRequest commentRequest = new StudentCommentRequest("Bob001@gmail.com",
                "I like learning English.");

        // When
        when(commentService.createComment(isA(StudentCommentRequest.class))).thenReturn(studentComment);

        // Then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(jsonPath("$.id").value(studentComment.getId()))
                .andExpect(jsonPath("$.name").value(studentComment.getName()))
                .andExpect(jsonPath("$.email").value(studentComment.getEmail()))
                .andExpect(jsonPath("$.value").value(studentComment.getValue()))
                .andExpect(jsonPath("$.created").value(studentComment.getCreated().toString()))
                .andExpect(status().isCreated());
        verify(commentService, times(1)).createComment(isA(StudentCommentRequest.class));
    }

    @Test
    public void testCreateCommentThrowsMethodArgumentNotValidException() throws Exception {

        // Given
        StudentCommentRequest invalidRequest = new StudentCommentRequest("invalid_email", "");

        // Then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(jsonPath("$.errorCode").value(17))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateCommentThrowsEmailNotFoundException() throws Exception {

        // When
        when(commentService.createComment(isA(StudentCommentRequest.class))).thenThrow(
                new EmailNotFoundException("Student with email Bob001@gmail.com not found"));

        // Then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentComment)))
                .andExpect(jsonPath("$.errorCode").value(5))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCommentThrowsNoPurchasedLessonsException() throws Exception {

        // When
        when(commentService.createComment(isA(StudentCommentRequest.class))).thenThrow(
                new NoPurchasedLessonsException("The user did not purchase any lessons"));

        // Then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentComment)))
                .andExpect(jsonPath("$.errorCode").value(15))
                .andExpect(status().isForbidden());
    }
}
