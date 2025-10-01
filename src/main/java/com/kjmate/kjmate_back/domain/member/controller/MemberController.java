package com.kjmate.kjmate_back.domain.member.controller;

import com.kjmate.kjmate_back.domain.member.dto.*;
import com.kjmate.kjmate_back.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberJoinDto member) {
        MemberResponse memberResponse = memberService.signUp(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest,
                                                  HttpServletResponse httpServletResponse) {
        LoginResponseDto loginResponseDto = memberService.login(loginRequest);

        // Refresh Token을 HttpOnly Cookie에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);  // true로 하면 https 프로토콜에서만 전송인데 false로 해서 테스트 환경에서도 되게
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);  // 7일
        httpServletResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(loginResponseDto.builder()
                .accessToken(loginResponseDto.getAccessToken())
                .memberId(loginResponseDto.getMemberId())
                .email(loginResponseDto.getEmail())
                .nickname(loginResponseDto.getNickname())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            Authentication authentication,
            @RequestHeader("Authorization") String authHeader,
            HttpServletResponse httpServletResponse) {
        String email = authentication.getName();
        String assessToken = authHeader.substring(7);
        memberService.logout(email, assessToken);

        // Refresh Token 쿠키 삭제
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 바로 삭제
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken){
        if(refreshToken != null){
            throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
        }
        TokenResponseDto response = memberService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
