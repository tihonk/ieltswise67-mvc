package com.ieltswise.controller.request;

import com.ieltswise.dto.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Schema(
        title = "Request to create a tutor",
        description = "An object containing data for creating a new tutor"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorCreateRequest {

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "email",
            description = "Tutor email, with pre-open access to the calendar",
            example = "test.tutor1.ieltswise67@gmail.com"
    )
    @Email(message = "should be valid")
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String email;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = "The name of the tutor",
            example = "Neil"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String name;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "The tutor's schedule for the week with the specified busy and free hours of the day",
            example = "{\"MONDAY\": [{\"time\": \"09:00\", \"engaged\": false}]," +
                    " \"TUESDAY\": [{\"time\": \"10:00\", \"engaged\": true}]}"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private Map<DayOfWeek, List<TimeSlot>> updatedTimeInfo;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = """
                    The client ID that is used to authenticate the application when interacting with the PayPal API""",
            example = "AcTtU8_JdRJdA2B6eDsAsVj896ZkFM34F0xsyJFz6HJwZtIw4MMluLX"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientId;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            description = """
                    The secret key that is used to authenticate the application when interacting with the PayPal API""",
            example = "EDVanIUr5KRtKq_Sgh7tkQg_1Q_wIv51n_kO2lOppUF0C_9mV1nv"
    )
    @NotEmpty(message = "should not be empty")
    @NotNull(message = "is required")
    private String clientSecret;
}
