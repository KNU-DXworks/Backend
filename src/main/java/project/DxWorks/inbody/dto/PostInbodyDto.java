package project.DxWorks.inbody.dto;


public record PostInbodyDto(
        String privateKey,
        String createdAt,
        String gender,
        double weight,
        double muscleMass,
        double fatRatio,
        String muscleMassType,
        String fatMassType,
        String userCase,
        String armMuscleType,
        String trunkMuscleType,
        String legMuscleType){
}
