package com.practice.cafe.service;

import com.practice.cafe.dto.error.ErrorResponseDto;
import com.practice.cafe.dto.request.user.SignUpRequestDto;
import com.practice.cafe.dto.response.user.UserStatusResponseDto;
import com.practice.cafe.exception.ErrorException;
import com.practice.cafe.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        ;
    }

    @DisplayName("개인정보를 받아서 회원가입한다.")
    @Test
    void signupUser() {
        // given
        LocalDateTime signupDateTime = LocalDateTime.now();

        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .email("shj1602@naver.com")
                .password("qwer123!")
                .name("송현진")
                .address("서울")
                .phone("01012345678")
                .admin(true)
                .adminKey("ZKVPrhksflWKdlqslend")
                .build();

        // when
        ResponseEntity<?> responseEntity = userService.signUp(requestDto);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isInstanceOf(UserStatusResponseDto.class);

        UserStatusResponseDto responseDto = (UserStatusResponseDto) responseEntity.getBody();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getName()).isEqualTo("송현진");
        assertThat(responseDto.getMessage()).isEqualTo("회원가입을 성공했습니다.");
        assertThat(responseDto.getSignupDateTime()).isCloseTo(signupDateTime, within(1, ChronoUnit.SECONDS));

        // User 저장 여부 확인
        assertThat(userRepository.findByEmail("shj1602@naver.com")).isPresent();
    }

    @DisplayName("이메일이 중복되는 경우 예외가 발생한다.")
    @Test
    void checkUserDuplicationEmail() {
        // given
        SignUpRequestDto requestDto1 = SignUpRequestDto.builder()
                .email("shj1602@naver.com")
                .password("qwer123!")
                .name("송현진")
                .address("서울")
                .phone("01012345678")
                .admin(true)
                .adminKey("ZKVPrhksflWKdlqslend")
                .build();
        userService.signUp(requestDto1);

        SignUpRequestDto requestDto2 = SignUpRequestDto.builder()
                .email("shj1602@naver.com")
                .password("qwer123!")
                .name("서현진")
                .address("서울")
                .phone("01012345678")
                .admin(false)
                .adminKey("")
                .build();

        // when
        ResponseEntity<?> responseEntity = userService.signUp(requestDto2);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isInstanceOf(ErrorResponseDto.class);

        ErrorResponseDto errorResponseDto = (ErrorResponseDto) responseEntity.getBody();
        assertThat(errorResponseDto).isNotNull();
        assertThat(errorResponseDto.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponseDto.getMessage()).isEqualTo("이미 사용 중인 이메일입니다.");
    }

    @DisplayName("잘못된 관리자 키로 회원가입 시 예외가 발생한다.")
    @Test
    void checkInvalidAdminKey() {
        // given
        SignUpRequestDto requestDto = SignUpRequestDto.builder()
                .email("shj1602@naver.com")
                .password("qwer123!")
                .name("송현진")
                .address("서울")
                .phone("01012345678")
                .admin(true)
                .adminKey("adminKey")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.signUp(requestDto))
                .isInstanceOf(ErrorException.class)
                .hasMessage("관리자 키가 일치하지 않습니다.");
    }
}