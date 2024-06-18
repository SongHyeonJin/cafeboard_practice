package com.practice.cafe.controller;

import com.practice.cafe.dto.request.user.LoginRequestDto;
import com.practice.cafe.dto.request.user.SignUpRequestDto;
import com.practice.cafe.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafe")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequestDto requestDto){
        return userService.signUp(requestDto);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto,
                                   HttpServletResponse response){
        return userService.login(requestDto, response);
    }
}
