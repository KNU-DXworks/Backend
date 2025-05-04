package project.DxWorks.goal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GoalRequestDto {
    private Long userId;
    private Double weight;
    private Double muscle;
    private Double fat;
    private Double bmi;
    private Double arm;
    private Double body;
    private Double leg;
    private Double goalGroup;
}
