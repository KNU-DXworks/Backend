package project.DxWorks.profile.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.post.dto.PostAllResponseDto;
import project.DxWorks.post.dto.PostResponseDto;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.profile.dto.*;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

//TODO : communityId와 아직 연동 x
@Service
@RequiredArgsConstructor
public class IntroduceService {

    private final ProfileRepository profileRepository;
    private final ContractDeployService contractDeployService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Web3j web3j;


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

    //조회 (profileId로 조회 -> 다른 사람 프로필 조회)
    @Transactional
    public IntroduceResponseDto getIntroduce(Long profileId) throws IOException {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필ID가 존재하지 않습니다.: " + profileId));

        UserEntity user = profile.getUser();
        String userName = user.getUserName(); //userName 변수 추가.

        // getInbody 인자에 지갑주소 들어갈 예정
       List<InbodyDto> inbodySet = contractDeployService.getInbody(profile.getWalletAddress());

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
                profile.getCommunity(),
                history,
                inbody,
                posts,
                userName);

        return dto;
    }


    //내 프로필 조회 (userId 사용)
    @Transactional
    public IntroduceMyResponseDto getMyIntroduce(Long userId) throws IOException {

        //userId로 UserEntity 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다."));

        //유저로 프로필 조회
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 프로필이 존재하지 않습니다."));

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
        String eth = null;

        // 지갑이 등록된 경우
        if (profile.getWalletAddress() != null){
            eth = getWalletBalance(profile.getWalletAddress());
        }

        IntroduceMyResponseDto dto = new IntroduceMyResponseDto(
                profile.getId(),
                profile.getIntroduce(),
                profile.getCommunity(),
                eth,
                history,
                inbody,
                posts,
                user.getUserName());

        return dto;
    }

    //수정
    //TODO : 한 User는 프로필 1개(OnetoOne)를 가지므로 userId를 통해 수정? 삭제도 마찬가지?
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

    // 지갑 등록
    @Transactional
    public Response<String> postWallet(long userId, PostWalletRequestDto dto) {

        Profile profile = profileRepository.findByUser(
                userRepository.findById(userId).orElseThrow(
                        () -> new IllegalArgumentException("해당하는 유저가 없습니다.")
                )).orElseThrow(() -> new IllegalArgumentException("프로필을 먼저 생성해주세요"));

        profile.setWalletAddress(dto.getWalletAddress());

        profileRepository.save(profile);

        return Response.ok("지갑이 정상적으로 등록됐습니다.");
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

    // 잔액 정보 가져오기
    public String getWalletBalance(String walletAddress) throws IOException {
        EthGetBalance balanceResponse = web3j.ethGetBalance(
                walletAddress, DefaultBlockParameterName.LATEST
        ).send();

        BigInteger balanceWei = balanceResponse.getBalance();
        BigDecimal balanceEth = new BigDecimal(balanceWei).divide(BigDecimal.TEN.pow(18));

        return balanceEth.stripTrailingZeros().toPlainString();
    }

}







