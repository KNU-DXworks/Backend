package project.DxWorks.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.apache.catalina.User;
import project.DxWorks.common.repository.TimeBaseEntity;

import java.util.List;

@Entity
@Getter
public class UserRecommend extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long recommendUserId;

    private String reason;

    @Builder
    public UserRecommend(Long userId, Long recommendUserId, String reason) {
        this.userId = userId;
        this.recommendUserId = recommendUserId;
        this.reason = reason;
    }




    public UserRecommend() {
    }
}
