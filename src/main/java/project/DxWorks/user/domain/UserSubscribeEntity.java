package project.DxWorks.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicUpdate
public class UserSubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserEntity fromUser;

    @ManyToOne
    private UserEntity toUser;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    public UserSubscribeEntity(UserEntity fromUser, UserEntity toUser, Integer days){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusDays(days);
    }

    /**
     * 구독 기간 유효여부 확인
     */
    public boolean isValid() {
        return expiresAt == null || expiresAt.isAfter(LocalDateTime.now());
    }
}
