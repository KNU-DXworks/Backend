package project.DxWorks.goal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.DxWorks.goal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
