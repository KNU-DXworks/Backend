package project.DxWorks.profile.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.post.dto.PostAllResponseDto;
import project.DxWorks.post.dto.PostResponseDto;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.profile.dto.HistoryDto;
import project.DxWorks.profile.dto.IntroduceRequestDto;
import project.DxWorks.profile.dto.IntroduceResponseDto;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

//TODO : communityId와 아직 연동 x
@Service
@RequiredArgsConstructor
public class IntroduceService {

    private final ProfileRepository profileRepository;
    private final ContractDeployService contractDeployService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //자기소개 등록
    @Transactional
    public IntroduceResponseDto createIntroduce(IntroduceRequestDto requestDto, Long userId) {
        Profile profile = Profile.builder().
                introduce(requestDto.getIntroduce()).
                user(userRepository.findById(userId).
                        orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다. "))).
                        build();
        Profile saved = profileRepository.save(profile);

        return toDto(saved);
    }

    //조회
    @Transactional
    public IntroduceResponseDto getIntroduce(Long profileId) throws IOException {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필ID가 존재하지 않습니다.: " + profileId));

        UserEntity user = profile.getUser();
        String userName = user.getUserName(); //userName 변수 추가.

        // getInbody 인자에 지갑주소 들어갈 예정
       List<InbodyDto> inbodySet = contractDeployService.getInbody(profile.getWalletAddress());

       // 인바디 히스토리 정보
        List<HistoryDto> history = inbodySet.stream()
                .map(dto -> new HistoryDto(dto.createdAt(), dto.userCase()))
                .toList();

        // 포스트 정보 불러오기
        List<PostAllResponseDto> posts = postRepository.findAllByUser(user).stream()
                .map(post -> new PostAllResponseDto(
                    post.getId(),
                    post.getUser().getUserName(),
                    post.getRegDt(),
                    post.getPostType(),
                    post.getCommunityType(),
                    post.getContent(),
                    post.getPostImg(),
                    resolveFileType(post.getPostImg())
                ))
                .toList();

        InbodyDto inbody = null;

        if (!inbodySet.isEmpty()){
            inbody = inbodySet.get(inbodySet.size() - 1);
        }

        IntroduceResponseDto dto = new IntroduceResponseDto(
                profileId,
                profile.getIntroduce(),
                profile.getCommunityId(),
                history,
                inbody,
                posts,
                userName);

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

    private String resolveFileType(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return "NONE";
        if (fileUrl.endsWith(".pdf")) return "application/pdf";
        if (fileUrl.matches(".*\\.(jpg|jpeg|png|gif|bmp)$")) return "image/jpeg";
        return "UNKNOWN";
    }

}







