package project.DxWorks.profile.service;


import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.repository.UserRepository;

// TODO : 자기소개 등록 / 수정 기능 구현
@Service
public class IntroduceService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public IntroduceService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

//    // 자기 소개 등록 / 수정
//    public boolean updateIntroduce(long userId, String introduce) {
//        //userId로 프로필 찾기
//        Profile profile = profileRepository.findById(userId).orElse(null);
//        if (user != null) {
//            profile.setIntroduce(introduce); //자기소개 수정
//            profileRepository.save(profile); // 저장
//            return true;
//        }
//        return false;
//    }
}

    // TODO : goal 도메인에서 DTO 가져오기






