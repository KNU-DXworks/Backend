package project.DxWorks.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.profile.service.IntroduceService;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final IntroduceService introduceService;

//    @PostMapping("/introduce/{userId}")
//    public ResponseEntity<String> introduce(@PathVariable long userId,@RequestBody IntroduceRequestDto introduceRequestDto) {
//        //userId와 introduce를 이용해 자기소개 등록
//        boolean isUpdated = introduceService.updateIntroduce(introduceRequestDto.getUserId(),introduceRequestDto.getIntroduce());
//
//        if(isUpdated) {
//            return ResponseEntity.ok("자기소개가 성공적으로 등록되었습니다.");
//        }else{
//            return ResponseEntity.status(400).body("자기소개 등록에 실패했습니다.");
//        }
//    }
}
