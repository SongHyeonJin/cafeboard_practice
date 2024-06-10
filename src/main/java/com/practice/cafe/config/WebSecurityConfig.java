package com.practice.cafe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors(Customizer.withDefaults()) // cors 설정(기본값)
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/cafe","/cafe/users/**").permitAll()
                        .anyRequest().authenticated()
                );
        return httpSecurity.build();
    }

}
