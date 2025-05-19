package project.DxWorks.post.dto.response.myProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.post.dto.response.UserPostResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class MyProfileResponseDto {
    private long userId;

    private String userName;

    private CommunityCategory bodyType;

    private String profileImg;

    private String eth;

    private String info;

    private boolean isWalletRegistered;

    private GoalDto goal;

    private List<InbodyDto> inbody;

    private List<UserPostResponseDto> posts;
}
