package project.DxWorks.userRecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformationRecordDto {
    private Long userId;
    private String username;
    private String startGroup;
    private String endGroup;
    private double weightEnd;
    private double muscleEnd;
    private double fatEnd;
    private LocalDate completedAt;
}
