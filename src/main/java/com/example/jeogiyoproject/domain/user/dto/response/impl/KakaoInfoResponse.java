package com.example.jeogiyoproject.domain.user.dto.response.impl;

import com.example.jeogiyoproject.global.auth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse{

    @JsonProperty("kakao_account")
    private KakaoAccount KakaoAccount;

    public KakaoInfoResponse(KakaoAccount kakaoAccount) {
        this.KakaoAccount = kakaoAccount;
    }

    @Getter
    @JsonIgnoreProperties (ignoreUnknown = true)
    public static class KakaoAccount {
        private KakaoProfile profile;
        private String email;

        public KakaoAccount(KakaoProfile profile, String email) {
            this.profile = profile;
            this.email = email;
        }
    }

    @Getter
    @JsonIgnoreProperties (ignoreUnknown = true)
    public static class KakaoProfile {
        private String nickname;

        public KakaoProfile(String nickname) {
            this.nickname = nickname;
        }
    }

    @Override
    public String getEmail() {
        return KakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return KakaoAccount.profile.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
