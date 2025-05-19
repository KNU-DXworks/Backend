package project.DxWorks.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.DxWorks.common.repository.TimeBaseEntity;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.post.domain.PostType;
import project.DxWorks.user.domain.UserEntity;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    private CommunityCategory communityType;

    @Column(nullable = false)
    private String content;

    private String postImg;

    private PostType postType;

    public Post(UserEntity user, CommunityCategory communityType, String content, String postImg, PostType postType) {
        this.user = user;
        this.communityType = communityType;
        this.content = content;
        this.postImg = postImg;
        this.postType = postType;
    }
}
