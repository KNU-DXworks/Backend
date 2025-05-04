package project.DxWorks.domain.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

