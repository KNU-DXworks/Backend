package project.DxWorks.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.dto.IntroduceResponseDto;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.profile.service.IntroduceService;

import java.io.IOException;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final IntroduceService introduceService;

    //등록
    @PostMapping("/introduce")
    public ResponseEntity<IntroduceResponseDto> create(@RequestBody IntroduceRequestDto dto){
        IntroduceResponseDto created = introduceService.createIntroduce(dto);
        return ResponseEntity.ok(created);
    }

    //조회
    @GetMapping("/{profileId}")
    public ResponseEntity<IntroduceResponseDto> get(@PathVariable long profileId) throws IOException {
        IntroduceResponseDto introduce = introduceService.getIntroduce(profileId);
        return ResponseEntity.ok(introduce);
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

}
