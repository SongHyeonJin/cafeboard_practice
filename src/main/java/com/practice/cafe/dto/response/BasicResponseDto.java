package com.practice.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "add")
public class BasicResponseDto {

    private int statusCode;
    private String message;

    public static BasicResponseDto success(int statusCode, String message){
        return BasicResponseDto.add(statusCode, message);
    }

    public static BasicResponseDto badRequest(String message){
        return BasicResponseDto.add(HttpStatus.BAD_REQUEST.value(), message);
    }
}
