package project.DxWorks.GeminiAI.dto;

import java.util.List;

public record RecommendationResponseDto(
        List<RecommendationDto> recommand
) {
}
