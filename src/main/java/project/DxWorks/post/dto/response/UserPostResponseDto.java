package project.DxWorks.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.post.domain.PostType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserPostResponseDto {
    private long postId;

    private String profileImg;

    private String userName;

    private LocalDateTime date;

    private PostType postType;

    private CommunityCategory communityType;

    private String content;

    private String fileUrl;

    private String fileType;

}
