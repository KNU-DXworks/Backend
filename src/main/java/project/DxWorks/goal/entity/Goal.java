package project.DxWorks.goal.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private Double weight;

    private Double muscle;

    private Double fat;

    private Double bmi;

    //TODO : 팔, 몸통, 다리 근육 Enum 형식으로 설정.
    @Enumerated(EnumType.STRING)
    private BodyTypeLevel armGrade;

    @Enumerated(EnumType.STRING)
    private BodyTypeLevel bodyGrade;

    @Enumerated(EnumType.STRING)
    private BodyTypeLevel legGrade;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType; //목표체형그룹

}
