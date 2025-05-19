package project.DxWorks.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.post.domain.CommunityType;
import project.DxWorks.post.dto.PostAllResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CommunityPostAllRequestDto {
    private CommunityType communityType;
    List<PostAllResponseDto> posts;
}
