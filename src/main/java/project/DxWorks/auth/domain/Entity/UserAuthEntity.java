package project.DxWorks.auth.domain.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.DxWorks.common.repository.TimeBaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_user_auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class UserAuthEntity extends TimeBaseEntity {

    @Id
    private Long userId;
    private Long kakaoId;
    private String refreshToken;
    private LocalDateTime lastloginDt;

    public UserAuthEntity(Long userId, Long kakaoId, String refreshToken) {
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.refreshToken = refreshToken;
    }

    public void updateLastLoginAt() {
        this.lastloginDt = LocalDateTime.now();
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
