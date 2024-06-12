package com.practice.cafe.entity;

import com.practice.cafe.dto.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("회원가입 시 권한은 일반 회원이다.")
    @Test
    void roleUser(){
        // given
        String email = "shj401@naver.com";
        String password = "qwer123!";
        String name = "송현진";
        String address = "서울";
        String phone = "01012345678";
        UserRole role = UserRole.USER;

        // when
        User user = User.signup(email, password, name, address, phone, role);

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.USER);

     }

    @DisplayName("회원가입 시 권한은 관리자이다.")
    @Test
    void roleAdmin(){
        // given
        String email = "shj401@naver.com";
        String password = "qwer123!";
        String name = "송현진";
        String address = "서울";
        String phone = "01012345678";
        UserRole role = UserRole.ADMIN;

        // when
        User user = User.signup(email, password, name, address, phone, role);

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);

    }



}