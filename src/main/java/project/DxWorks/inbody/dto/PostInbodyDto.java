package project.DxWorks.inbody.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record PostInbodyDto(
        long id,
        LocalDateTime createdAt,
        boolean inbodySheet,
        String gender,
        double weight,
        double height,
        double muscle,
        double fat,
        double bmi,
        String bodyType,
        String armGrade,        // 이름 변경됨 (armMuscleType → armGrade)
        String bodyGrade,       // 이름 변경됨 (trunkMuscleType → bodyGrade)
        String legGrade,        // 이름 변경됨 (legMuscleType → legGrade)
        String privateKey
) {}
