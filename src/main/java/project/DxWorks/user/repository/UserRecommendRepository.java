package project.DxWorks.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.user.domain.UserRecommend;

import java.util.List;
import java.util.Optional;

public interface UserRecommendRepository extends JpaRepository<UserRecommend, Long> {

    Optional<List<UserRecommend>> findAllByUserId(Long userId);
    void deleteAllByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
