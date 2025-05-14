package project.DxWorks.GeminiAI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //inbody id


    private LocalDateTime createdAt; //등록 일시

    private String gender; // 성별 (남,녀 별 8가지 클래스로 나누는 기준이 달라서 필요.)
    private double weight; //몸무게
    private double muscleMass; //골격근량
    private double fatMass; // 체지방량
    private double bmi;             // BMI
    private double muscleMassRatio; // 골격근량 / 체중 * 100 -> 체형분류할때 사용됨.
    private double fatRatio; //체지방률! 체지방률 / 체중 * 100 -> 체형분류

    private String muscleMassType; //골격근량 표준인지 표준이하인지
    private String fatMassType;  // 체지방량 표준인지 표준이하인지
    // private String bmiType; 없어도됨.

    private String userCase; //사용자의 체형 유형

    private String armMuscleType;       // 팔근육 유형
    private String trunkMuscleType;     // 몸통근육 유형
    private String legMuscleType;       // 다리근육 유형


}
