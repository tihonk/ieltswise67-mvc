package com.ieltswise.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentCommentDto {
    private String studentEmail;
    private String value;
}
