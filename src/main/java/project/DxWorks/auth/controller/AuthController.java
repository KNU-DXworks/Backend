package project.DxWorks.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.auth.dto.KakaoAuthRequestDto;
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

}
