package project.DxWorks.inbody.struct;

import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

public class InbodyStruct extends DynamicStruct {
    public final Utf8String createdAt;
    public final Utf8String gender;
    public final Uint256 weight;
    public final Uint256 muscleMass;
    public final Uint256 fatRatio;
    public final Utf8String muscleMassType;
    public final Utf8String fatMassType;
    public final Utf8String userCase;
    public final Utf8String armMuscleType;
    public final Utf8String trunkMuscleType;
    public final Utf8String legMuscleType;

    public InbodyStruct(
            Utf8String createdAt,
            Utf8String gender,
            Uint256 weight,
            Uint256 muscleMass,
            Uint256 fatRatio,
            Utf8String muscleMassType,
            Utf8String fatMassType,
            Utf8String userCase,
            Utf8String armMuscleType,
            Utf8String trunkMuscleType,
            Utf8String legMuscleType
    ) {
        super(createdAt, gender, weight, muscleMass, fatRatio,
                muscleMassType, fatMassType, userCase,
                armMuscleType, trunkMuscleType, legMuscleType);
        this.createdAt = createdAt;
        this.gender = gender;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.fatRatio = fatRatio;
        this.muscleMassType = muscleMassType;
        this.fatMassType = fatMassType;
        this.userCase = userCase;
        this.armMuscleType = armMuscleType;
        this.trunkMuscleType = trunkMuscleType;
        this.legMuscleType = legMuscleType;
    }
}