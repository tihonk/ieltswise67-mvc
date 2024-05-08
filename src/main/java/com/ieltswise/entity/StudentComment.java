package com.ieltswise.entity;

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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDENT_COMMENT")
public class StudentComment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name = "EMAIL", nullable = false)
    private String email;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "VALUE", nullable = false, length = 1000)
    private String value;
    @Column(nullable = false)
    private LocalDateTime created;
}
