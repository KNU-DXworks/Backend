package project.DxWorks.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.post.entity.Post;
import project.DxWorks.user.domain.UserEntity;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCommunityType(CommunityCategory community);

    List<Post> findAllByUser(UserEntity user);

    @Query("SELECT p FROM Post p WHERE p.user IN :subscribedUsers")
    List<Post> findByUserIn(List<UserEntity> subscribedUsers);
}
