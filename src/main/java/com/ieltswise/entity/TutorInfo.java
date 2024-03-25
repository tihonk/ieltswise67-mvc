package com.ieltswise.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ieltswise.entity.schedule.Schedule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TUTOR_INFO")
public class TutorInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;
    @Column(name = "NAME")
    private String name;
    @Column(nullable = false)
    private Long created;
    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Schedule schedule;
    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private PaymentCredentials paymentCredentials;
}
