package project.DxWorks.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.DxWorks.community.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
}
