package project.DxWorks.domain.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.DxWorks.domain.community.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Long, Community> {
}
