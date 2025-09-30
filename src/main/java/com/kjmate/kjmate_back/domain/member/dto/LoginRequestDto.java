package com.kjmate.kjmate_back.domain.member.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String password;
}
