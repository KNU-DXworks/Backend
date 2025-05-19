package project.DxWorks.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.auth.domain.JwtTokenProvider;
import project.DxWorks.auth.domain.KakaoUserInfo;
import project.DxWorks.auth.dto.KakaoTokenResponseDto;
import project.DxWorks.auth.dto.UserAccessTokenResponseDto;
import project.DxWorks.auth.interfacese.UserAuthInterface;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserAuthInterface userAuthInterface;
    private final RestTemplate restTemplate;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    @Transactional
    public UserAccessTokenResponseDto processKakaoLogin(String kakaoAccessToken) {
        // 카카오 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);


        // 사용자 정보 없을 시 예외처리
        if (kakaoUserInfo == null || kakaoUserInfo.getId() == null){
            throw new IllegalArgumentException("Invalid kakao token");
        }

        Long kakaoId = kakaoUserInfo.getId();

        // 유저정보 확인 후 가입 및 처리
        UserAuthEntity userAuthEntity = userAuthInterface.findByKakaoId(kakaoUserInfo.getId())
                .orElseGet(() -> userAuthInterface.registerUser(kakaoUserInfo.toUser(), kakaoId, kakaoUserInfo.getProperties().getProfile_image()));


        // 토큰 생성
        UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthEntity.getUserId().toString()); // 별도의 고유 이름이 없기에 ID로 find
        String accessToken = jwtTokenProvider.createAccessToken(userDetails, userAuthEntity.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userAuthEntity.getUserId());

        return new UserAccessTokenResponseDto(accessToken, refreshToken, jwtTokenProvider.getTokenValidTime());
    }

    @Transactional
    public KakaoTokenResponseDto kakaoTokenRequest(String authCode){
        try{
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("redirect_uri", "http://localhost:5173/redirection");
            params.add("code", authCode);
            params.add("client_secret", clientSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                    "https://kauth.kakao.com/oauth/token",
                    request,
                    KakaoTokenResponseDto.class
            );

            return response.getBody();
        } catch(Exception e){
            throw new RuntimeException("카카오 토큰을 불러오는데 실패했습니다.", e);
        }
    }


    private KakaoUserInfo getKakaoUserInfo(String kakaoAccessToken) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(kakaoAccessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserInfo.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Kakao user profile", e);
        }
    }
}
