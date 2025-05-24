package project.DxWorks.auth.domain.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import project.DxWorks.common.repository.TimeBaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "com_user_tele")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TelegramAuthEntity extends TimeBaseEntity {
    @Id
    private Long userId;
    private Long telegramId;
    private String refreshToken;
    private LocalDateTime lastloginDt;

    public TelegramAuthEntity(Long userId, Long telegramId, String refreshToken) {
        this.userId = userId;
        this.telegramId = telegramId;
        this.refreshToken = refreshToken;
    }
}
