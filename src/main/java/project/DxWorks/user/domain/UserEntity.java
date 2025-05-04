package project.DxWorks.user.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.DxWorks.common.repository.TimeBaseEntity;
import project.DxWorks.goal.entity.Goal;

@Entity
@Builder
@Table(name="com_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;

    @OneToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    public void modifyEmail(String email){
        this.email = email;
    }

}
