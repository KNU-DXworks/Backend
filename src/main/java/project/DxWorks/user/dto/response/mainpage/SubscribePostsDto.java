package project.DxWorks.user.dto.response.mainpage;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SubscribePostsDto {
    private long postId;

    private long userId;

    private String userName;

    private String profileImg;

    private LocalDateTime createdDate;

    private CommunityCategory bodyType;

    private String content;

    private String fileUrl;

    private String fileType;

}
