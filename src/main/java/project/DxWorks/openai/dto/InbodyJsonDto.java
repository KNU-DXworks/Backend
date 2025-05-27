package project.DxWorks.openai.dto;

import lombok.Data;
import project.DxWorks.GeminiAI.entity.Inbody;

@Data
public class InbodyJsonDto {
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

    public InbodyJsonDto(Inbody inbody) {
        this.gender = inbody.getGender();
        this.height = inbody.getHeight();
        this.weight = inbody.getWeight();
        this.muscle = inbody.getMuscle();
        this.fat = inbody.getFat();
        this.bmi = inbody.getBmi();
        this.muscleMassType = inbody.getMuscleMassType();
        this.fatMassType = inbody.getFatMassType();
        this.armGrade = inbody.getArmGrade();
        this.bodyGrade = inbody.getBodyGrade();
        this.legGrade = inbody.getLegGrade();
    }

    // Getter 생략 가능 (Jackson은 필드 직접 접근 가능)
}

