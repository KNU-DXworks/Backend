package project.DxWorks.post.dto;

import project.DxWorks.post.domain.CommunityType;
import project.DxWorks.post.domain.PostType;

import java.time.LocalDateTime;

public record PostAllResponseDto(
        long postId,
        String userName,
        LocalDateTime date,
        PostType postType,
        CommunityType communityType,
        String content,
        String fileUrl,
        String fileType
) {
}
