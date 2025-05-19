package project.DxWorks.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.post.dto.PostAllResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CommunityPostAllRequestDto {
    private CommunityCategory communityType;
    List<PostAllResponseDto> posts;
}
