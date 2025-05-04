package project.DxWorks.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.user.domain.UserInterestEntity;

public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {

}
