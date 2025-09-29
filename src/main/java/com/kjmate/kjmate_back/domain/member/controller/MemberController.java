package com.kjmate.kjmate_back.domain.member.controller;

import com.kjmate.kjmate_back.domain.member.dto.MemberJoinDto;
import com.kjmate.kjmate_back.domain.member.dto.MemberResponse;
import com.kjmate.kjmate_back.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
