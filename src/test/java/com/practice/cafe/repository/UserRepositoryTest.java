package com.practice.cafe.repository;

import com.practice.cafe.dto.UserRole;
import com.practice.cafe.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void findByEmail(){
        // given
        User user1 = User.signup("shj401@naver.com", "qwer123!", "송현진", "서울", "01012345678", UserRole.ADMIN);
        User user2 = User.signup("kmu207@naver.com", "qwer123!", "김민욱", "부산", "01012345679", UserRole.USER);
        User user3 = User.signup("hsw1402@naver.com", "qwer123!", "황선완", "부산", "01012345670", UserRole.USER);
        userRepository.saveAll(List.of(user1, user2, user3));

        // when
        Optional<User> findUser =userRepository.findByEmail("shj401@naver.com");

        // then
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getEmail()).isEqualTo(user1.getEmail());

     }

}