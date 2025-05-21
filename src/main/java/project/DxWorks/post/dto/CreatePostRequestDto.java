package project.DxWorks.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.post.domain.PostType;

@Data
@AllArgsConstructor
public class CreatePostRequestDto {

    private CommunityCategory communityType;

    private PostType postType;

    private String content;

    public CreatePostRequestDto() {}
}
