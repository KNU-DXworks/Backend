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
    private Double weight;
    private Double muscle;
    private Double fat;
    private Double bmi;
    private String armGrade;
    private String bodyGrade;
    private String legGrade;
    private String bodyType;
    private Long userId;

    public static GoalResponseDto fromEntity(Goal goal) {
        GoalResponseDto dto = new GoalResponseDto();
        dto.goalId = goal.getGoalId();
        dto.weight = goal.getWeight();
        dto.muscle = goal.getMuscle();
        dto.fat = goal.getFat();
        dto.bmi = goal.getBmi();
        dto.armGrade = String.valueOf(goal.getArmGrade());
        dto.bodyGrade = String.valueOf(goal.getBodyGrade());
        dto.legGrade = String.valueOf(goal.getLegGrade());
        dto.bodyType = String.valueOf(goal.getBodyType());

        return dto;
    }
}
