package project.DxWorks.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Community {

    @Id
    @GeneratedValue
    @Column(name = "community_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommunityCategory communityCategory;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();


    public void addPost(Post post) {
        posts.add(post);
        post.setCommunity(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setCommunity(null);
    }
}

