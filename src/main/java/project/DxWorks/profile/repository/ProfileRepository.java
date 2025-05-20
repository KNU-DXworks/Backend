package project.DxWorks.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.user.domain.UserEntity;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(UserEntity user);

    Optional<Profile> findByWalletAddress(String walletAddress);
}
