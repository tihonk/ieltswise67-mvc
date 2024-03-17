package com.ieltswise.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentCommentRequest {

    private String email;
    private String value;
}
