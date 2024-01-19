package com.ieltswise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDENT_COMMENT")
public class StudentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String studentEmail;
    @Column(name = "NAME", nullable = false)
    private String studentName;
    @Column(name = "STUDENT_COMMENT", nullable = false)
    private String comment;
    @Column(nullable = false)
    private LocalDateTime created;
}
