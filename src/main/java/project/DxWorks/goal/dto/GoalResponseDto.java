package project.DxWorks.goal.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Double arm;
    private Double body;
    private Double leg;
    private Double goalGroup;
    private Long userId;
}
