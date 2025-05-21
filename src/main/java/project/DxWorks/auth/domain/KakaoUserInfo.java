package project.DxWorks.auth.domain;

import lombok.Getter;
import project.DxWorks.user.domain.UserEntity;

@Getter
public class KakaoUserInfo {
    private Long id;
    private KakaoProperties properties;

    public UserEntity toUser() {
        return UserEntity.builder()
                .userName(properties.getNickname())
                .build();
    }
}
