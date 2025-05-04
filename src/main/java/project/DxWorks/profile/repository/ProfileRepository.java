package project.DxWorks.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    //User Id로 Profile 조회 (1:1 매핑)
    //Optional<Profile> findByUserId(Long userId);

}
