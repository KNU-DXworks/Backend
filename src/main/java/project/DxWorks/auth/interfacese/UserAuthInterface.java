package project.DxWorks.auth.interfacese;

import project.DxWorks.auth.domain.Entity.TelegramAuthEntity;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.user.domain.UserEntity;

import java.util.Optional;

public interface UserAuthInterface {
    UserAuthEntity registerUser(UserEntity userEntity, Long kakaoId, String profileImage);

    Optional<TelegramAuthEntity> findByUserId(Long userId);

    Optional<UserAuthEntity> findByKakaoId(Long kakaoId);

    TelegramAuthEntity registerTelegramUser(String name,
                                            Long telegramId,
                                            String profileImage,
                                            String userName);

}
