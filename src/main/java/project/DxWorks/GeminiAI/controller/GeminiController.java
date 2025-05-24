package project.DxWorks.GeminiAI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.GeminiAI.dto.RecommendationResponseDto;
import project.DxWorks.GeminiAI.service.GeminiService;
import project.DxWorks.common.ui.Response;

import java.util.Map;


@RestController
@RequestMapping("/api/auth/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/recommend")
    public Response<RecommendationResponseDto> recommendSimilarUsers(@RequestAttribute Long userId, @RequestBody Map<String, Object> input) {
        RecommendationResponseDto result = new RecommendationResponseDto(geminiService.recommendSimilarUsersByTrajectory(userId, input));

        return Response.ok(result);
    }
}
