package com.kjmate.kjmate_back.domain.member.service;

import com.kjmate.kjmate_back.domain.member.dto.*;
import com.kjmate.kjmate_back.domain.member.entity.Member;
import com.kjmate.kjmate_back.domain.member.repository.MemberRepository;
import com.kjmate.kjmate_back.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    //회원가입
    public MemberResponse signUp(MemberJoinDto memberJoinDto) {
        if (memberRepository.existsByEmail(memberJoinDto.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일 입니다.");
        }

        if (memberRepository.existsByNickname(memberJoinDto.getNickname())) {
            throw new IllegalArgumentException("존재하는 닉네임 입니다.");
        }

        Member member = Member.builder()
                .email(memberJoinDto.getEmail())
                .nickname(memberJoinDto.getNickname())
                .password(passwordEncoder.encode(memberJoinDto.getPassword()))
                .nationality(memberJoinDto.getNationality())
                .name(memberJoinDto.getName())
                .profileImageUrl("default.png")
                .createdAt(LocalDateTime.now())
                .build();
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
        }

        // 기존 리프레쉬 토큰 지우기
        redisService.deleteRefreshToken(member.getEmail());

        // accessToken 및 refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(member.getEmail(), member.getId(), member.getNationality());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        // 레디스에 리프레쉬 토큰 저장
        redisService.saveRefreshToken(member.getEmail(), refreshToken, jwtUtil.getRefreshTokenExpiration());

        return LoginResponseDto.builder().
                accessToken(accessToken).
                refreshToken(refreshToken).
                email(member.getEmail()).
                memberId(member.getId()).
                nickname(member.getNickname()).
                build();
    }

    // 로그아웃
    public void logout(String email, String accessToken) {
        redisService.deleteRefreshToken(email);

        long expirationTime = jwtUtil.getExpirationTime(accessToken);
        if (expirationTime > 0){
            redisService.addBlackList(accessToken, expirationTime);
        }
        log.info("로그아웃 성공:{}" , email);
    }

    // 갱신
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        // 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // refresh Token 이메일 추출
        String email = jwtUtil.getEmailFromToken(refreshToken);

        // Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = redisService.getRefreshToken(email);
        if (storedRefreshToken == null || !refreshToken.equals(storedRefreshToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
        }
        
        // 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다."));

        String accessToken = jwtUtil.generateAccessToken(email, member.getId(), member.getNationality());
        log.info("Access Token 갱신 성공: {}", email);

        return TokenResponseDto.builder().
                accessToken(accessToken).
                build();
    }
}
