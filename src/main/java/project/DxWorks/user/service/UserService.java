package project.DxWorks.user.service;


import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.GeminiAI.service.InbodyService;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.contract.InbodySmartContract;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.domain.UserInterestEntity;
import project.DxWorks.user.dto.request.ModifyUserInfRequestDto;
import project.DxWorks.user.dto.response.InterestUserListResponseDto;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.repository.UserInterestRepository;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserInterestRepository userInterestRepository;

    private final ProfileRepository profileRepository;

    private final ContractDeployService contractDeployService;

    @Transactional
    public Response<UserInfResponseDto> getUserInf(Long userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        return Response.ok(new UserInfResponseDto(userEntity.getUserName(), userEntity.getEmail()));
    }

    @Transactional
    public Response<String> modifyUserEmail(Long userId, ModifyUserInfRequestDto requestDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        userEntity.modifyEmail(requestDto.email());

        return Response.ok("이메일 변경이 완료됐습니다.");
    }

    @Transactional
    public Response<String> interestUser(Long toUser, Long fromUser){
        UserEntity user1 = userRepository.findById(toUser)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        UserEntity user2 = userRepository.findById(fromUser)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        userInterestRepository.save(new UserInterestEntity(user1, user2));

        return Response.ok("관심사용자 등록이 완료됐습니다.");
    }

    @Transactional
    public Response<String> deleteInterestUser(Long toUser, Long fromUser) {
        UserEntity user1 = userRepository.findById(toUser)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        UserEntity user2 = userRepository.findById(fromUser)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        UserInterestEntity interest = userInterestRepository.findByToUserAndFromUser(user1, user2);
        userInterestRepository.delete(interest);

        return Response.ok("관심사용자에서 제거됐습니다.");
    }

    @Transactional
    public Response<List<InterestUserListResponseDto>> getInterestUser(Long userId, boolean type){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));

        List<UserEntity> toUser;

        if (type){ // 관심사용자 목록
            toUser = userInterestRepository.findToUsersByFromUser(user);
        }
        else{ // 나를 관심있어 하는 사용자 목록
            toUser = userInterestRepository.findFromUsersByToUser(user);
        }

        return Response.ok(toUser.stream()
                .map(u -> {
                    Profile profile = profileRepository.findByUser(u)
                            .orElseThrow(() -> new IllegalArgumentException("해당유저가 존재하지 않습니다."));
                    try {
                        List<InbodyDto> inbodys = contractDeployService.getInbody(profile.getWalletAddress());

                        return new InterestUserListResponseDto(
                                u.getId(),
                                u.getUserName(),
                                profile.getProfileUrl(),
                                inbodys.get(inbodys.size()-2).userCase(),
                                profile.getCommunity().toString()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()));
    }
}
