package project.DxWorks.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.DxWorks.UserRecommend.service.RecommendService;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.post.service.PostService;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.dto.response.mainpage.*;
import project.DxWorks.user.repository.UserInterestRepository;
import project.DxWorks.user.repository.UserRepository;
import project.DxWorks.user.repository.UserSubscibeRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;
    private final UserSubscibeRepository userSubscibeRepository;
    private final ProfileRepository profileRepository;
    private final ContractDeployService contractDeployService;
    private final PostRepository postRepository;
    private final PostService postService;
    private final RecommendService recommendService;

    public Response<MainPageResponseDto> mainPage(long userId) throws IOException {

        UserEntity myUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        // interestUser
        List<InterestUserDto> interestUserDtoList = userInterestRepository.findToUsersByFromUser(myUser)
                .stream()
                .map(user -> {
                    Profile profile = profileRepository.findByUser(user)
                            .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다. 관리자에게 문의해주세요."));

                    return new InterestUserDto(
                            user.getId(),
                            user.getUserName(),
                            profile.getProfileUrl(),
                            profile.getCommunity()
                    );
                })
                .toList();

        // subscribeUser
        List<SubscribeUserDto> subscribeUserDtoList = userSubscibeRepository.findToUsersByFromUser(myUser)
                .stream()
                .map(user -> {
                    Profile profile = profileRepository.findByUser(user)
                            .orElseThrow(() -> new IllegalArgumentException("프로필이 존재하지 않습니다. 관리자에게 문의해주세요."));

                    try {
                        List<InbodyDto> inbodys = contractDeployService.getInbody(profile.getWalletAddress());

                        return new SubscribeUserDto(
                                user.getId(),
                                user.getUserName(),
                                profile.getProfileUrl(),
                                inbodys.get(0).userCase(),
                                profile.getCommunity()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        List<RecommendUserDto> recomandUserDtos = recommendService.recommendUserByGoal(userId);



        // 구독자 게시물

        List<UserEntity> users = userSubscibeRepository.findToUsersByFromUser(myUser);
        List<SubscribePostsDto> subscribePostsDtos = postRepository.findByUserIn(users)
                .stream()
                .map(post -> {
                        UserEntity user = post.getUser();
                        Profile profile = profileRepository.findByUser(user)
                                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 존재하지 않습니다."));

                        String fileType = postService.resolveFileType(post.getPostImg());
                        return new SubscribePostsDto(
                                post.getId(),
                                user.getId(),
                                user.getUserName(),
                                profile.getProfileUrl(),
                                post.getRegDt(),
                                profile.getCommunity(),
                                post.getContent(),
                                post.getPostImg(),
                                fileType
                        );
                })
                .toList();

        return Response.ok(new MainPageResponseDto(
                interestUserDtoList,
                subscribeUserDtoList,
                recomandUserDtos,
                subscribePostsDtos
        ));
    }

}
