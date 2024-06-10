package com.practice.cafe.dto.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소를 입력해주세요.")
    @NotNull(message = "이메일을 필수 입력 값입니다.")
    private String email;

    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자에서 15자 사이로만 가능합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]*$", message = "비밀번호는 영문, 숫자, 특수문자를 전부 포함해야 합니다.")
    @NotNull(message = "비밀번호를 필수 입력 값입니다.")
    private String password;

    @Size(min = 2, max = 10, message = "이름은 2~10자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 영문 또는 한글만 허용됩니다.")
    @NotNull(message = "이름을 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "주소를 필수 입력 값입니다.")
    private String address;

    @NotNull(message = "전화번호를 필수 입력 값입니다.")
    @Size(min = 11, max = 11, message = "휴대폰 번호는 11자리여야 합니다.")
    private String phone;

    private boolean admin = false;
    private String adminKey;
}
