package project.DxWorks.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.common.ui.Response;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.dto.request.ModifyUserInfRequestDto;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
