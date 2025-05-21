package project.DxWorks.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.post.dto.PostAllResponseDto;

import java.util.List;

@AllArgsConstructor
@Data
public class IntroduceMyResponseDto {
    private long profileId;
    private String introduce; //조회 응답용.
    private CommunityCategory community;

    private String eth;

    private List<HistoryDto> history;

    private InbodyDto inbody;

    private List<PostAllResponseDto> posts;

    private String userName;
}
