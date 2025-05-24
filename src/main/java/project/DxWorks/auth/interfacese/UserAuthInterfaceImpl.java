package project.DxWorks.auth.interfacese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.DxWorks.auth.domain.Entity.TelegramAuthEntity;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.auth.repository.TelegramAuthRepository;
import project.DxWorks.auth.repository.UserAuthRepository;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserAuthInterfaceImpl implements UserAuthInterface {

    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final TelegramAuthRepository telegramAuthRepository;

    @Override
    public UserAuthEntity registerUser(UserEntity userEntity, Long kakaoId, String profileImage){
        userRepository.save(userEntity);

        Profile profile = new Profile(
                null,
                "안녕하세요.",
                profileImage,
                null,
                CommunityCategory.NONE,
                userEntity);

        profileRepository.save(profile);

        return userAuthRepository.save(new UserAuthEntity(userEntity.getId(), kakaoId, null));
    }

    @Override
    public Optional<TelegramAuthEntity> findByUserId(Long userId){
        return telegramAuthRepository.findById(userId);
    }

    @Override
    public Optional<UserAuthEntity> findByKakaoId(Long kakaoId){
        return userAuthRepository.findByKakaoId(kakaoId);
    }

    @Override
    public TelegramAuthEntity registerTelegramUser(String name, Long telegramId, String profileImage, String userName){
        UserEntity user = new UserEntity(name, userName);

        userRepository.save(user);

        Profile profile = new Profile(
                null,
                "안녕하세요.",
                profileImage,
                null,
                CommunityCategory.NONE,
                user
        );

        profileRepository.save(profile);

        return telegramAuthRepository.save(new TelegramAuthEntity(user.getId(), telegramId, null));
    }

}
