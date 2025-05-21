package project.DxWorks.post.dto;

import project.DxWorks.user.domain.UserEntity;

public record PostRequestDto(
        Long userId,
        String content,
        String postImg
) {}