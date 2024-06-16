package com.practice.cafe.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserStatusResponseDto {

    private String name;
    private String message;
    private LocalDateTime signupDateTime;
}
