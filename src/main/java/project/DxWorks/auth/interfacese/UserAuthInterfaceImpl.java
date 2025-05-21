package project.DxWorks.auth.interfacese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
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
    public Optional<UserAuthEntity> findByUserId(Long userId){
        return userAuthRepository.findById(userId);
    }

    @Override
    public Optional<UserAuthEntity> findByKakaoId(Long kakaoId){
        return userAuthRepository.findByKakaoId(kakaoId);
    }

}
