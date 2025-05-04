package project.DxWorks.domain.profile.service;


import org.springframework.stereotype.Service;
import project.DxWorks.domain.profile.entity.Profile;
import project.DxWorks.domain.profile.repository.ProfileRepository;

// TODO : 자기소개 등록 / 수정 기능 구현
@Service
public class IntroduceService {

    private final ProfileRepository profileRepository;

    public IntroduceService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // 자기 소개 등록 / 수정

    public void updateProfile(Long Userid,String introduce){
        Profile profile = profileRepository.findById(Userid).orElse(null);

        profile.setIntroduce(introduce);
        profileRepository.save(profile);
    }




}
