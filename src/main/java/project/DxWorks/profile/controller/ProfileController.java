package project.DxWorks.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.common.ui.Response;
import project.DxWorks.profile.dto.IntroduceMyResponseDto;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.dto.IntroduceResponseDto;
import project.DxWorks.profile.dto.PostWalletRequestDto;
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
    public ResponseEntity<IntroduceResponseDto> create(@RequestAttribute Long userId, @RequestBody IntroduceRequestDto dto){
        IntroduceResponseDto created = introduceService.createIntroduce(dto, userId);
        return ResponseEntity.ok(created);
    }

    //타인 프로필 조회
    @GetMapping("/{profileId}")
    public ResponseEntity<IntroduceResponseDto> get(@PathVariable long profileId) throws IOException {
        IntroduceResponseDto introduce = introduceService.getIntroduce(profileId);
        return ResponseEntity.ok(introduce);
    }

    //내 프로필 조회
    @GetMapping("/myprofile")
    public ResponseEntity<IntroduceMyResponseDto> getMyProfile(@RequestAttribute Long userId) throws IOException {

//        //Spring Security 사용하여 Authentication 객채에서 userName 꺼내오고 , userName으로 userId찾음.
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        Object principal = auth.getPrincipal(); //principle -> String or UserEntity
//        String userName;
//
//        if (principal instanceof UserEntity) {
//            userName = ((UserEntity) principal).getUserName();
//        } else if(principal instanceof String) {
//            userName = (String) principal; //일반적으로는 userName임.
//        } else {
//            throw new IllegalArgumentException("해당 사용자 정보를 찾을 수 없습니다.");
//        }


//        //userName을 이용해 userEntity 조회
//        UserEntity user = userRepository.findByUserName(userName)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 정보를 찾을 수 없습니다."));
        IntroduceMyResponseDto dto = introduceService.getMyIntroduce(userId);

        return ResponseEntity.ok(dto);
    }

    //수정
    @PutMapping("/{profileId}")
    public ResponseEntity<IntroduceResponseDto> update(@PathVariable long profileId, @RequestBody IntroduceRequestDto dto){
        IntroduceResponseDto updated = introduceService.updateIntroduce(profileId, dto);
        return ResponseEntity.ok(updated);
    }

    //삭제
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> delete(@PathVariable long profileId){
        introduceService.deleteIntroduce(profileId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wallet")
    public Response<String> postWallet(@RequestAttribute long userId, PostWalletRequestDto dto){
        return introduceService.postWallet(userId, dto);
    }

}
