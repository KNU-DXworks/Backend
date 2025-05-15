package project.DxWorks.userRecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendRequestDto {
    private String startGroup;       // 현재 체형 그룹
    private String goalGroup;        // 목표 체형 그룹
    private double targetWeight;  // 목표 체중
    private double targetMuscle;  // 목표 골격근량
    private double targetFat;     // 목표 지방량
}
