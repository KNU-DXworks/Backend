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

    private String armMuscle;       // 팔근육
    private String trunkMuscle;     // 몸통근육
    private String legMuscle;       // 다리근육


}
