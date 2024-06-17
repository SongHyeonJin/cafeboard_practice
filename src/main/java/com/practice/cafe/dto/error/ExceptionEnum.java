package com.practice.cafe.dto.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    USER_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 이메일입니다."),
    NOT_ADMIN_KEY(HttpStatus.BAD_REQUEST.value(), "관리자 키가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "잘못된 비밀번호입니다.");

    private final int status;
    private final String message;

    ExceptionEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
