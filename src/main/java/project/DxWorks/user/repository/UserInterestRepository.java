package project.DxWorks.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.domain.UserInterestEntity;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {

    @Query("SELECT u.toUser FROM UserInterestEntity u WHERE u.fromUser = :fromUser")
    List<UserEntity> findToUsersByFromUser(@Param("fromUser") UserEntity fromUser);

    @Query("SELECT u.fromUser FROM UserInterestEntity u WHERE u.toUser = :toUser")
    List<UserEntity> findFromUsersByToUser(@Param("toUser") UserEntity toUser);

    boolean existsByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);

}
