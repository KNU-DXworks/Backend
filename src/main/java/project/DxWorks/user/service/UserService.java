package project.DxWorks.user.service;


import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.common.ui.Response;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.domain.UserInterestEntity;
import project.DxWorks.user.dto.request.ModifyUserInfRequestDto;
import project.DxWorks.user.dto.response.InterestUserListResponseDto;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.repository.UserInterestRepository;
import project.DxWorks.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserInterestRepository userInterestRepository;

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
                .map(u -> new InterestUserListResponseDto(u.getId(), u.getUserName()))
                .collect(Collectors.toList()));
    }

    public Goal getUserGoal(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getGoal();
    }
}
