package com.example.jeogiyoproject.global.auth;

import com.example.jeogiyoproject.domain.user.dto.response.impl.OAuthInfoResponse;
import org.json.simple.parser.ParseException;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();
    String requestAccessToken (OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo (String accessToken) throws ParseException;
}
