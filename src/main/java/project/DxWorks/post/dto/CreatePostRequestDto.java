package project.DxWorks.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.post.domain.CommunityType;
import project.DxWorks.post.domain.PostType;

@Data
@AllArgsConstructor
public class CreatePostRequestDto {

    private CommunityType communityType;

    private PostType postType;

    private String content;

    public CreatePostRequestDto() {}
}
