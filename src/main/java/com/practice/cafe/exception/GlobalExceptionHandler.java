package com.practice.cafe.exception;

import com.practice.cafe.dto.error.ErrorResponseDto;
import com.practice.cafe.dto.response.BasicResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valid 예외처리
     * BasicResponseDto를 사용해 유효성 검사 오류에 대한 에러 응답
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> signupValidException(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()){
            builder.append("[");
            builder.append(error.getField());
            builder.append("]");
            builder.append(error.getDefaultMessage());
        }
        return new ResponseEntity<>(BasicResponseDto.badRequest(builder.toString()), HttpStatus.BAD_REQUEST);
    }

    /**
     *  ErrorException 에외처리
     *  ErrorResponseDto를 사용해 ErrorException에 대한 에러 응답
     */
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleErrorException(ErrorException error){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(error.getExceptionEnum().getStatus(), error.getMessage());
        return ResponseEntity.status(error.getExceptionEnum().getStatus()).body(errorResponseDto);
    }
}
