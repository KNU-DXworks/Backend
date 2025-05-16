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
    private String arm;
    private String body;
    private String leg;
    private String goalBody;
    private Long userId;
}
