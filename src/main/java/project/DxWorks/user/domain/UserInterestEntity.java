package project.DxWorks.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
public class UserInterestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserEntity fromUser;

    @ManyToOne
    private UserEntity toUser;

    public UserInterestEntity(UserEntity fromUser, UserEntity toUser){
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

}
