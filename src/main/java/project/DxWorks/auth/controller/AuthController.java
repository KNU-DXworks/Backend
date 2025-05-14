package project.DxWorks.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.auth.dto.KakaoAuthRequestDto;
import project.DxWorks.auth.dto.KakaoTokenResponseDto;
import project.DxWorks.auth.dto.UserAccessTokenResponseDto;
import project.DxWorks.auth.service.KakaoAuthService;
import project.DxWorks.common.ui.Response;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

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



}
