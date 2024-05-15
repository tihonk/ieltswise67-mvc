package com.ieltswise.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
        title = "Student comment",
        description = "Represents a comment made by a student about a lesson."
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDENT_COMMENT")
public class StudentComment {

    @Schema(
            description = "Unique identifier for the student comment.",
            example = "1"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Schema(
            type = "string",
            format = "email",
            description = "Email of the student the lesson is planned",
            example = "BobRTY145@gmail.com"
    )
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Schema(
            description = "Name of the student who made the comment",
            example = "Bob"
    )
    @Column(name = "NAME", nullable = false)
    private String name;

    @Schema(
            description = "The content of the student's comment",
            example = "I like learning English!"
    )
    @Column(name = "VALUE", nullable = false, length = 1000)
    private String value;

    @Schema(
            description = "The date and time when the comment was created.",
            example = "2024-05-02 20:19:35.100000"
    )
    @Column(nullable = false)
    private LocalDateTime created;
}
