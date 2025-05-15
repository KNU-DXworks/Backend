package project.DxWorks.goal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.user.domain.UserEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Goal {

    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private Double weight;

    private Double muscle;

    private Double fat;

    private Double bmi;

    //TODO : 팔, 몸통, 다리 근육 Enum 형식으로 설정.
    @Enumerated(EnumType.STRING)
    private BodyTypeLevel arm;

    @Enumerated(EnumType.STRING)
    private BodyTypeLevel body;

    @Enumerated(EnumType.STRING)
    private BodyTypeLevel leg;

    @Enumerated(EnumType.STRING)
    private BodyType goalBody; //목표체형그룹

//    public Number getGoalGroup() {
//    }
}
