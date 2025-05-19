package project.DxWorks.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.DxWorks.user.domain.UserEntity;

import java.util.List;

public interface UserSubscibeRepository {
    @Query("SELECT u.toUser FROM UserInterestEntity u WHERE u.fromUser = :fromUser")
    List<UserEntity> findToUsersByFromUser(@Param("fromUser") UserEntity fromUser);

    @Query("SELECT u.fromUser FROM UserInterestEntity u WHERE u.toUser = :toUser")
    List<UserEntity> findFromUsersByToUser(@Param("toUser") UserEntity toUser);
}
