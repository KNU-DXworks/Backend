package project.DxWorks.profile.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import project.DxWorks.GeminiAI.service.InbodyService;
import project.DxWorks.inbody.dto.PostInbodyRequestDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.inbody.struct.InbodyStruct;
import project.DxWorks.profile.dto.HistoryDto;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.dto.IntroduceResponseDto;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO : communityId와 아직 연동 x
@Service
@RequiredArgsConstructor
public class IntroduceService {

    private final ProfileRepository profileRepository;
    private final ContractDeployService contractDeployService;

    //자기소개 등록
    @Transactional
    public IntroduceResponseDto createIntroduce(IntroduceRequestDto requestDto) {
        Profile profile = Profile.builder().
                introduce(requestDto.getIntroduce()).
                //communityId(requestDto.getCommunityId).
                        build();
        Profile saved = profileRepository.save(profile);
        //TODO : community id get 해야함.
        return toDto(saved);
    }

    //조회
    @Transactional
    public IntroduceResponseDto getIntroduce(Long profileId) throws IOException {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필ID가 존재하지 않습니다.: " + profileId));

        // getInbody 인자에 지갑주소 들어갈 예정
       List<PostInbodyRequestDto> inbodySet = contractDeployService.getInbody(profile.getWalletAddress());

        List<HistoryDto> history = inbodySet.stream()
                .map(dto -> new HistoryDto(dto.createdAt(), dto.userCase()))
                .toList();


        IntroduceResponseDto dto = new IntroduceResponseDto(
                profileId,
                profile.getIntroduce(),
                profile.getCommunityId(),
                history,
                inbodySet.get(inbodySet.size()-1));
        return dto;
    }

    //수정
    @Transactional
    public IntroduceResponseDto updateIntroduce(Long profileId,IntroduceRequestDto requestDto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));
        profile.setIntroduce(requestDto.getIntroduce());
        //profile.setCommunityId(requestDto.getCommunityId);
        Profile saved = profileRepository.save(profile);

        return toDto(saved);
    }

    //삭제
    @Transactional
    public void deleteIntroduce(Long profileId) { //profile introduce 삭제
        profileRepository.deleteById(profileId);
    }

    private IntroduceResponseDto toDto(Profile profile) {
        return IntroduceResponseDto.builder()
                .profileId(profile.getId())
                .introduce(profile.getIntroduce())
                //.communityId(profile.getCommunityId())
                .build();
    }

}







