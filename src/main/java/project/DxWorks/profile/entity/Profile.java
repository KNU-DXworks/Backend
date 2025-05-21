package project.DxWorks.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import project.DxWorks.community.entity.Community;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.user.domain.UserEntity;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // profile id

    @Column(length = 1000)
    private String introduce; //자기소개

    private String profileUrl;

    private String walletAddress;

    private CommunityCategory community;

    @OneToOne
    private UserEntity user;

    public boolean isWalletEmpty(){
        if (walletAddress == null){
            return true;
        }
        else{
            return false;
        }
    }

}