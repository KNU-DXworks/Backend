package project.DxWorks.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.DxWorks.common.repository.TimeBaseEntity;

@Entity
@Table(name="com_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;


}
