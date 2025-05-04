package project.DxWorks.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.DxWorks.post.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCommunityId(Long communityId);
}
