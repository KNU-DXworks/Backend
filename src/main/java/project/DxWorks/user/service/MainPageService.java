package project.DxWorks.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.DxWorks.common.ui.Response;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.dto.response.mainpage.InterestUserDto;
import project.DxWorks.user.dto.response.mainpage.MainPageResponseDto;
import project.DxWorks.user.repository.UserInterestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {
    private final UserInterestRepository userInterestRepository;
    private final ProfileRepository profileRepository;

    public Response<MainPageResponseDto> mainPage(long userId) {

        // interestUser
        List<InterestUserDto> interestUserDtoList = userInterestRepository.findToUsersByFromUser(userId)
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
                });

        // subscribeUser


    }

}
