package project.DxWorks.auth.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.auth.dto.KakaoAuthRequestDto;
import project.DxWorks.auth.dto.KakaoTokenResponseDto;
import project.DxWorks.auth.dto.UserAccessTokenResponseDto;
import project.DxWorks.auth.service.KakaoAuthService;
import project.DxWorks.auth.service.TelegramAuthService;
import project.DxWorks.common.ui.Response;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final TelegramAuthService telegramAuthService;

    @PostMapping("/kakao")
    public Response<UserAccessTokenResponseDto> authenticateWithKakao(@RequestBody KakaoAuthRequestDto requestDto){
        UserAccessTokenResponseDto response = kakaoAuthService.processKakaoLogin(requestDto.accessToken());
        return Response.ok(response);
    }

    @GetMapping("/oauth/kakao/callback")
    public Response<KakaoTokenResponseDto> kakaoTokenReqeust(@RequestParam("code") String authCode){
        KakaoTokenResponseDto response = kakaoAuthService.kakaoTokenRequest(authCode);
        return Response.ok(response);
    }

    @PostMapping("/telegram")
    public Response<UserAccessTokenResponseDto> authenticateWithTelegram(@RequestBody Map<String, String> body) throws JsonProcessingException {
        String initData = body.get("initData");
        UserAccessTokenResponseDto response = telegramAuthService.authenticateWithTelegram(initData);
        return Response.ok(response);
    }



}
