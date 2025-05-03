package project.DxWorks.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import project.DxWorks.common.repository.TimeBaseEntity;

@Entity
@Table(name="com_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicUpdate
public class UserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String email;


}
