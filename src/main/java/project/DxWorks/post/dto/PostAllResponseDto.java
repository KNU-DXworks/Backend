package project.DxWorks.post.dto;

import project.DxWorks.community.entity.CommunityCategory;

import java.time.LocalDateTime;

public record PostAllResponseDto(
        long postId,
        long userId,
        String userName,
        String profileImg,
        LocalDateTime createdDate,
        CommunityCategory bodyType,
        String content,
        String fileUrl,
        String fileType
) {
}
