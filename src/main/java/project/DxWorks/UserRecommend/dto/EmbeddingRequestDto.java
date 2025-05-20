package project.DxWorks.UserRecommend.dto;


import project.DxWorks.inbody.dto.InbodyDto;

import java.util.List;

public record EmbeddingRequestDto(
        long userId,
        List<Double> vector
){}