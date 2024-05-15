package com.ieltswise.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Tutor Info",
        description = "Represents the information about a tutor, including email, name, and related entities " +
                "such as schedule and payment credentials."
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TUTOR_INFO")
public class TutorInfo {

    @Schema(
            description = "Unique identifier for the tutor",
            example = "1"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Schema(
            type = "string",
            format = "email",
            description = "Tutor email, with pre-open access to the calendar",
            example = "test.tutor1.ieltswise67@gmail.com"
    )
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Schema(
            type = "string",
            description = "The name of the tutor",
            example = "Neil"
    )
    @Column(name = "NAME")
    private String name;

    @Schema(
            description = "Timestamp of when the tutor was created",
            example = "1735689600000"
    )
    @Column(nullable = false)
    private Long created;

    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Schedule schedule;

    @OneToOne(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private PaymentCredentials paymentCredentials;
}
