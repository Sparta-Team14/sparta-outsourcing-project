package com.example.jeogiyoproject.global.auth;

import com.example.jeogiyoproject.domain.user.dto.response.impl.KakaoInfoResponse;
import com.example.jeogiyoproject.domain.user.dto.response.impl.OAuthInfoResponse;
import com.example.jeogiyoproject.domain.user.entity.KakaoMember;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class KaKaoApiClient implements OAuthApiClient{
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String authUrl;

    @Value("${oauth.kakao.api.url}")
    private String apiUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redircetUrl;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", redircetUrl);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoTokens response = restTemplate.postForObject("https://kauth.kakao.com/oauth/token",request, KakaoTokens.class);

        assert response != null;
        return response.getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) throws ParseException {

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        header.put("Authorization", "Bearer " + accessToken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        String responseBody = get(userInfoUrl, header);
        System.out.println("responseBody = " + responseBody);
        //JsonObjcet
        JSONParser jsonParser = new JSONParser();
        JSONObject profiles = (JSONObject) jsonParser.parse(responseBody);
        JSONObject properties = (JSONObject) profiles.get("properties");
        JSONObject kakao_account = (JSONObject) profiles.get("kakao_account");

        Long loginId = (Long) profiles.get("id");
        String email = (String) kakao_account.get("email");
        String userName = (String) properties.get("nickname");

        return new KakaoInfoResponse();
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }



    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
