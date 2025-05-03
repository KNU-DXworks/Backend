package project.DxWorks.blockchain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostInbodyRequestDto {

    private long id;
    private String createdAt;
    private double weight;
    private double muscleMass;
    private double fatMass;
    private double bmi;
    private String armMuscle;
    private String trunkMuscle;
    private String legMuscle;
}
