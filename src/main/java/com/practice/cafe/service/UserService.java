package com.practice.cafe.service;

import com.practice.cafe.dto.UserRole;
import com.practice.cafe.dto.error.ErrorResponseDto;
import com.practice.cafe.dto.error.ExceptionEnum;
import com.practice.cafe.dto.request.user.LoginRequestDto;
import com.practice.cafe.dto.request.user.SignUpRequestDto;
import com.practice.cafe.dto.response.user.UserStatusResponseDto;
import com.practice.cafe.entity.User;
import com.practice.cafe.exception.ErrorException;
import com.practice.cafe.repository.UserRepository;
import com.practice.cafe.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_KEY = "ZKVPrhksflWKdlqslend";

    @Transactional
    public ResponseEntity<?> signUp(SignUpRequestDto signUpRequestDto){
        String email = signUpRequestDto.getEmail();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String name = signUpRequestDto.getName();
        String address = signUpRequestDto.getAddress();
        String phone = signUpRequestDto.getPhone();

        Optional<User> existUser = userRepository.findByEmail(email);
        if(existUser.isPresent())
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ExceptionEnum.USER_DUPLICATION));

        UserRole role = UserRole.USER;
        if(signUpRequestDto.isAdmin()){
            if(!signUpRequestDto.getAdminKey().equals(ADMIN_KEY))
                throw new ErrorException(ExceptionEnum.NOT_ADMIN_KEY);
            role = UserRole.ADMIN;
        }
        User user =  User.signup(email, password, name, address, phone, role);
        userRepository.save(user);
        return ResponseEntity.ok(new UserStatusResponseDto(user.getName(), "회원가입을 성공했습니다.", LocalDateTime.now()));
    }

    @Transactional
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto , HttpServletResponse response){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ErrorException(ExceptionEnum.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new ErrorException(ExceptionEnum.INVALID_PASSWORD);

        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getName(),user.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok(new UserStatusResponseDto(user.getName(), "로그인되었습니다.", LocalDateTime.now()));
    }

}
