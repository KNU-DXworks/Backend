package project.DxWorks.profile.dto;

import project.DxWorks.profile.entity.Profile;

public class IntroduceResponseDto {
    private String introduce; //조회 응답용.

    //생성자
    public IntroduceResponseDto(Profile profile) {
        this.introduce = profile.getIntroduce();
    }
}
