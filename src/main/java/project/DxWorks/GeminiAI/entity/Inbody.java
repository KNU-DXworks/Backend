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

    private boolean inbodySheet; //인바디 공식 종이 인지 아닌지 판별
    private String gender; // 성별 (남,녀 별 8가지 클래스로 나누는 기준이 달라서 필요.)
    private double weight; //몸무게
    private double height; //키
    private double muscle; //골격근량
    private double fat; // 체지방량
    private double bmi;     // BMI

    private String muscleMassType; //골격근량 표준인지 표준이하인지
    private String fatMassType;  // 체지방량 표준인지 표준이하인지

    private String bodyType; //사용자의 체형 유형

    private String armGrade;       // 팔근육 유형
    private String bodyGrade;     // 몸통근육 유형
    private String legGrade;       // 다리근육 유형


}
