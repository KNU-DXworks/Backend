package project.DxWorks.inbody.dto;

public record InbodyDto(
        String createdAt,
        String gender,
        double height,
        double weight,
        double muscle,
        double fat,
        double bmi,
        String userCase,
        String armGrade,
        String bodyGrade,
        String legGrade
) {}
