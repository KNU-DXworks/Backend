package project.DxWorks.goal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.goal.entity.BodyTypeLevel;
import project.DxWorks.goal.entity.Goal;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class GoalResponseDto {

    private Long goalId;
    private LocalDateTime createdDate;
    private Double weight;
    private Double muscle;
    private Double fat;
    private Double bmi;
    private String arm;
    private String body;
    private String leg;
    private String goalBody;
    private Long userId;

    public static GoalResponseDto fromEntity(Goal goal) {
        GoalResponseDto dto = new GoalResponseDto();
        dto.goalId = goal.getGoalId();
        dto.createdDate = goal.getCreatedDate();
        dto.weight = goal.getWeight();
        dto.muscle = goal.getMuscle();
        dto.fat = goal.getFat();
        dto.bmi = goal.getBmi();
        dto.arm = String.valueOf(goal.getArmGrade());
        dto.body = String.valueOf(goal.getBodyGrade());
        dto.leg = String.valueOf(goal.getLegGrade());
        dto.goalBody = String.valueOf(goal.getBodyType());

        return dto;
    }
}
