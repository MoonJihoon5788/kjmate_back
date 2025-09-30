package com.kjmate.kjmate_back.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String email;
    private String nickname;
}
