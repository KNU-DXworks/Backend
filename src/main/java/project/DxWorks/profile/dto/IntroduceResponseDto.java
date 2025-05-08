package project.DxWorks.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.profile.entity.Profile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IntroduceResponseDto {
    private long profileId;
    private String introduce; //조회 응답용.
}
