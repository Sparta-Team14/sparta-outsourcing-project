package com.example.jeogiyoproject.domain.user.dto.response.impl;

import com.example.jeogiyoproject.global.auth.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
