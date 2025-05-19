package project.DxWorks.user.dto.response.mainpage;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;

@Data
@AllArgsConstructor
public class RecomandUserDto {
    private long userId;

    private String userName;

    private String profileImg;

    private String prevType;

    private CommunityCategory bodyType;
}
