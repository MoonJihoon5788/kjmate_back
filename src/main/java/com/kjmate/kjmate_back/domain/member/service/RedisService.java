package com.kjmate.kjmate_back.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // 레디에 리프래쉬 토큰 저장
    public void saveRefreshToken(String email,String refreshToken,long expiresIn) {
        redisTemplate.opsForValue().set(
            "refreshToken:" + email,
                refreshToken,
                expiresIn,
                TimeUnit.MILLISECONDS
        );
    }

    // 리프래쉬 토큰 조회
    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get("refreshToken:" + email);
    }

    // 리프래쉬 토큰 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete("refreshToken:" + email);
    }

    // accessToken 블랙리스트 등록
    public void addBlackList(String accessToken, long expiresIn) {
        redisTemplate.opsForValue().set(
                "blackList:" + accessToken,
                "logout",
                expiresIn,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlackList(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blackList:" + accessToken));
    }
}
