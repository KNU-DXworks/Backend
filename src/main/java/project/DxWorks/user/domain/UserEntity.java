package project.DxWorks.user.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="com_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicUpdate
public class UserEntity {
}
