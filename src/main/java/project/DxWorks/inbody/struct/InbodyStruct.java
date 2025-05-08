package project.DxWorks.inbody.struct;

import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

public class InbodyStruct extends DynamicStruct {
    public Uint256 id;
    public Utf8String createdAt;
    public Uint256 weight;
    public Uint256 muscleMass;
    public Uint256 fatMass;
    public Uint256 bmi;
    public Utf8String armMuscle;
    public Utf8String trunkMuscle;
    public Utf8String legMuscle;

    public InbodyStruct(Uint256 id, Utf8String createdAt, Uint256 weight, Uint256 muscleMass,
                        Uint256 fatMass, Uint256 bmi, Utf8String armMuscle,
                        Utf8String trunkMuscle, Utf8String legMuscle) {
        super(id, createdAt, weight, muscleMass, fatMass, bmi, armMuscle, trunkMuscle, legMuscle);
        this.id = id;
        this.createdAt = createdAt;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.fatMass = fatMass;
        this.bmi = bmi;
        this.armMuscle = armMuscle;
        this.trunkMuscle = trunkMuscle;
        this.legMuscle = legMuscle;
    }
}