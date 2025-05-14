package project.DxWorks.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.inbody.dto.PostInbodyRequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IntroduceResponseDto {
    private long profileId;
    private String introduce; //조회 응답용.
    private long communityId;

    private List<HistoryDto> history;

    private PostInbodyRequestDto inbody;
}
