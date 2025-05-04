package project.DxWorks.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Community {

    @Id
    @GeneratedValue
    @Column(name = "community_id")
    private Long communityId;

    @Enumerated(EnumType.STRING)
    private CommunityCategory communityCategory;


}

