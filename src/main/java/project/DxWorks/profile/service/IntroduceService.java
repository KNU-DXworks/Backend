package project.DxWorks.profile.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.dto.IntroduceResponseDto;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.repository.UserRepository;

//TODO : communityId와 아직 연동 x
@Service
@RequiredArgsConstructor
public class IntroduceService {

    private final ProfileRepository profileRepository;

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
    public IntroduceResponseDto getIntroduce(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필ID가 존재하지 않습니다.: " + profileId));

        return toDto(profile);
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







