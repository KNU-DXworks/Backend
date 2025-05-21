package project.DxWorks.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.goal.entity.BodyType;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.post.dto.PostAllResponseDto;
import project.DxWorks.post.dto.response.UserPostResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class IntroduceResponseDto {
    private long profileId;
    private String introduce; //조회 응답용.
    private CommunityCategory community;

    private List<HistoryDto> history;

    private InbodyDto inbody;

    private List<PostAllResponseDto> posts;

    private String userName;


}
