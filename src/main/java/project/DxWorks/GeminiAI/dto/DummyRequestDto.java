package project.DxWorks.GeminiAI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DummyRequestDto {

    private String gender;
    private double height;
    private double weight;
    private double muscle;
    private double fat;
    private double bmi;
    private String muscleMassType;
    private String fatMassType;
    private String armGrade;
    private String bodyGrade;
    private String legGrade;

}
