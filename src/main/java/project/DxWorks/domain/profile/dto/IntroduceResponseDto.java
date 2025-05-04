package project.DxWorks.domain.profile.dto;

import project.DxWorks.domain.profile.entity.Profile;

public class IntroduceResponseDto {
    private String introduce; //조회 응답용.

    //생성자
    public IntroduceResponseDto(Profile profile) {
        this.introduce = profile.getIntroduce();
    }
}
