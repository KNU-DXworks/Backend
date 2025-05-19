package project.DxWorks.auth.interfacese;

import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.user.domain.UserEntity;

import java.util.Optional;

public interface UserAuthInterface {
    UserAuthEntity registerUser(UserEntity userEntity, Long kakaoId, String profileImage);

    Optional<UserAuthEntity> findByUserId(Long userId);

    Optional<UserAuthEntity> findByKakaoId(Long kakaoId);

}
