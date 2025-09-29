package com.kjmate.kjmate_back.domain.member.service;

import com.kjmate.kjmate_back.domain.member.dto.MemberJoinDto;
import com.kjmate.kjmate_back.domain.member.dto.MemberResponse;
import com.kjmate.kjmate_back.domain.member.entity.Member;
import com.kjmate.kjmate_back.domain.member.repository.MemberRepository;
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
}
