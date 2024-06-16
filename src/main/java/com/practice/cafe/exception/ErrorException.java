package com.practice.cafe.exception;

import com.practice.cafe.dto.error.ExceptionEnum;
import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException{

    private final ExceptionEnum exceptionEnum;

    public ErrorException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }
}
