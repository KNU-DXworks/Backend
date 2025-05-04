package project.DxWorks.goal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.DxWorks.goal.entity.Goal;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    // userId로 Goal 조회
    Optional<Goal> findByUserUserId(Long userId);
}
