package project.DxWorks.user.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
