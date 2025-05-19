package project.DxWorks.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.post.dto.response.UserPostResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OtherProfileResponseDto {
    private long userId;

    private String userName;

    private CommunityCategory bodyType;

    private boolean isLinked;

    private String profileImg;

    private String info;

    private List<InbodyDto> inbody;

    private List<UserPostResponseDto> posts;
}
