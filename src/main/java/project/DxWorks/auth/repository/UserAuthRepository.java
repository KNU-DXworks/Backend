package project.DxWorks.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
    Optional<UserAuthEntity> findByKakaoId(Long kakaoId);

    @Modifying
    @Query("update UserAuthEntity ua set ua.refreshToken = null where ua.userId = :userId")
    void expireRefreshToken(@Param("userId") Long userId);

    @Modifying
    @Query("update UserAuthEntity ua set ua.refreshToken = :refreshToken where ua.userId = :userId")
    void updateRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId);

}
