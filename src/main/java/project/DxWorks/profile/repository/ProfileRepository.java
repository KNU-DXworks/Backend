package project.DxWorks.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.profile.entity.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
