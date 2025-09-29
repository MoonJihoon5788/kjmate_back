package com.kjmate.kjmate_back.domain.member.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinDto {
    @NotBlank(message = "이메일은 필수 입력 장치입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자리여야 합니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 15, message = "이름은 2~15자리 이하여야합니다")
    private String name;
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 1, max = 15, message = "닉네임은 1~15자리 이하여야합니다")
    private String nickname;
    @NotNull
    private Character nationality;
}
