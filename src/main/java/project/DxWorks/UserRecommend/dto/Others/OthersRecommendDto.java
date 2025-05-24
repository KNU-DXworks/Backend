package project.DxWorks.UserRecommend.dto.Others;

import project.DxWorks.inbody.dto.InbodyRecommendDto;

import java.util.List;

public record OthersRecommendDto(
        Long userId,
        List<InbodyRecommendDto> history
) {
}
