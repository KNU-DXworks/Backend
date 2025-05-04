package project.DxWorks.post.dto;

import project.DxWorks.post.entity.Post;
import project.DxWorks.user.domain.UserEntity;

public record PostResponseDto(
        UserEntity user,
        String content,
        String postImg
) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getUser(),
                post.getContent(),
                post.getPostImg()
        );
    }
}

