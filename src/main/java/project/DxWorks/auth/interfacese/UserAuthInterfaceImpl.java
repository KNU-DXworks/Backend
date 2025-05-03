package project.DxWorks.auth.interfacese;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.auth.repository.UserAuthRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserAuthInterfaceImpl implements UserAuthInterface {

    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;

    @Override
    public UserAuthEntity registerUser(UserEntity userEntity, Long kakaoId){
        userRepository.save(userEntity);

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
