package project.DxWorks.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.auth.domain.JwtTokenProvider;
import project.DxWorks.auth.domain.KakaoUserInfo;
import project.DxWorks.auth.dto.UserAccessTokenResponseDto;
import project.DxWorks.auth.interfacese.UserAuthInterface;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserAuthInterface userAuthInterface;
    private final RestTemplate restTemplate;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

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
                .orElseGet(() -> userAuthInterface.registerUser(kakaoUserInfo.toUser(), kakaoId));

        // 토큰 생성
        UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthEntity.getUserId().toString()); // 별도의 고유 이름이 없기에 ID로 find
        String accessToken = jwtTokenProvider.createAccessToken(userDetails, userAuthEntity.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(userAuthEntity.getUserId());

        return new UserAccessTokenResponseDto(accessToken, refreshToken, jwtTokenProvider.getTokenValidTime());
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
