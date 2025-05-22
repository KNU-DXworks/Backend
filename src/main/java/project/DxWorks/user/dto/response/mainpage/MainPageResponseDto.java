package project.DxWorks.user.dto.response.mainpage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MainPageResponseDto {
    private List<InterestUserDto> interestUser;

    private List<SubscribeUserDto> subscribeUser;

    private List<RecommendUserDto> recommandUser;

    private List<SubscribePostsDto> subscribePosts;
}
