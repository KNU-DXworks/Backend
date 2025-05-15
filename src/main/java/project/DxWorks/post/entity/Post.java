package project.DxWorks.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.DxWorks.community.entity.Community;
import project.DxWorks.post.domain.PostType;
import project.DxWorks.user.domain.UserEntity;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private Community community;

    @Column(nullable = false)
    private String content;

    private String postImg;

    private PostType type;

    public Post(UserEntity user, Community community, String content, String postImg) {
        this.user = user;
        this.community = community;
        this.content = content;
        this.postImg = postImg;
    }
}
