package project.DxWorks.inbody.struct;

import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.List;

public class InbodyStruct extends DynamicStruct {
    public final Utf8String createdAt;
    public final Utf8String gender;
    public final Uint256 height;
    public final Uint256 weight;
    public final Uint256 muscle;
    public final Uint256 fat;
    public final Uint256 bmi;
    public final Utf8String userCase;
    public final Utf8String armGrade;
    public final Utf8String bodyGrade;
    public final Utf8String legGrade;

    // 필수 생성자 - Web3j가 디코딩할 때 이걸 사용함
    public InbodyStruct(
            Utf8String createdAt,
            Utf8String gender,
            Uint256 height,
            Uint256 weight,
            Uint256 muscle,
            Uint256 fat,
            Uint256 bmi,
            Utf8String userCase,
            Utf8String armGrade,
            Utf8String bodyGrade,
            Utf8String legGrade
    ) {
        super(createdAt, gender, height, weight, muscle, fat, bmi, userCase, armGrade, bodyGrade, legGrade);
        this.createdAt = createdAt;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.muscle = muscle;
        this.fat = fat;
        this.bmi = bmi;
        this.userCase = userCase;
        this.armGrade = armGrade;
        this.bodyGrade = bodyGrade;
        this.legGrade = legGrade;
    }

    // 선택: Tuple로 생성하는 보조 생성자도 만들 수 있음
    public InbodyStruct(List<Type> fields) {
        this(
                (Utf8String) fields.get(0),
                (Utf8String) fields.get(1),
                (Uint256) fields.get(2),
                (Uint256) fields.get(3),
                (Uint256) fields.get(4),
                (Uint256) fields.get(5),
                (Uint256) fields.get(6),
                (Utf8String) fields.get(7),
                (Utf8String) fields.get(8),
                (Utf8String) fields.get(9),
                (Utf8String) fields.get(10)
        );
    }
}