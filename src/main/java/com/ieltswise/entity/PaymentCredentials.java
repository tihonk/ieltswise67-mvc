package com.ieltswise.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        title = "Payment credentials",
        description = "Represents the payment credentials for a tutor, including PayPal client ID and secret"
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PAYMENT_CREDENTIALS")
public class PaymentCredentials {

    @Schema(
            description = "Unique identifier for the payment credentials",
            example = "1"
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Schema(
            description = """
                    The client ID that is used to authenticate the application when interacting with the PayPal API""",
            example = "AcTtU8_JdRJdA2B6eDsAsVj896ZkFM34F0xsyJFz6HJwZtIw4MMluLX"
    )
    @Column(name = "CLIENT_ID", unique = true, nullable = false)
    private String clientId;

    @Schema(
            description = """
                    The secret key that is used to authenticate the application when interacting with the PayPal API""",
            example = "EDVanIUr5KRtKq_Sgh7tkQg_1Q_wIv51n_kO2lOppUF0C_9mV1nv"
    )
    @Column(name = "CLIENT_SECRET", unique = true, nullable = false)
    private String clientSecret;

    @Schema(
            description = "The payment ID associated with the transaction",
            example = "PAYID-MYYOPEQ956840AAM8700040F"
    )
    @Column(name = "PAYMENT_ID", unique = true)
    private String paymentId;

    @Hidden
    @OneToOne
    @JoinColumn(name = "TUTOR_ID")
    @JsonBackReference
    private TutorInfo tutor;
}
