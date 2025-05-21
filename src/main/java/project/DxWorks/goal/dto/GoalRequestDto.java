package project.DxWorks.goal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GoalRequestDto {
    private Double weight;
    private Double muscle;
    private Double fat;
    private Double bmi;
    private String armGrade;
    private String bodyGrade;
    private String legGrade;
    private String bodyType;
}
