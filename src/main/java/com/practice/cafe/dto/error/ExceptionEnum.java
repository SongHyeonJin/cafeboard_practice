package com.practice.cafe.dto.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    USER_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    NOT_ADMIN_KEY(HttpStatus.BAD_REQUEST.value(), "관리자 암호가 일치하지 않습니다.");


    private final int status;
    private final String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
