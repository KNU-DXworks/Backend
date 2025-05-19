package project.DxWorks.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.common.ui.Response;
import project.DxWorks.post.dto.response.myProfile.MyProfileResponseDto;
import project.DxWorks.profile.dto.*;
import project.DxWorks.profile.dto.response.OtherProfileResponseDto;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.profile.service.IntroduceService;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final IntroduceService introduceService;
    private final UserRepository userRepository;

    //등록
    @PostMapping("/introduce")
    public Response<IntroduceResponseDto> create(@RequestAttribute Long userId, @RequestBody IntroduceRequestDto dto){
        IntroduceResponseDto created = introduceService.createIntroduce(dto, userId);
        return Response.ok(created);
    }

    //타인 프로필 조회
    @GetMapping("/{profileId}")
    public Response<OtherProfileResponseDto> get(@PathVariable long profileId, @RequestAttribute long userId) throws IOException {
        OtherProfileResponseDto introduce = introduceService.getIntroduce(profileId, userId);
        return Response.ok(introduce);
    }

    //내 프로필 조회
    @GetMapping("/myprofile")
    public Response<MyProfileResponseDto> getMyProfile(@RequestAttribute Long userId) throws IOException {

        MyProfileResponseDto dto = introduceService.getMyIntroduce(userId);

        return Response.ok(dto);
    }

    //수정
    @PutMapping("/update")
    public Response<UpdateIntroduceDto> update(@RequestAttribute Long userId, @RequestBody UpdateIntroduceDto dto){
        UpdateIntroduceDto updated = introduceService.updateIntroduce(userId, dto);
        return Response.ok(updated);
    }

    @PostMapping("/wallet")
    public Response<String> postWallet(@RequestAttribute long userId, @RequestBody PostWalletRequestDto dto){
        return introduceService.postWallet(userId, dto);
    }

}
