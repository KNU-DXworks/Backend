package project.DxWorks.UserRecommend.dto;


import java.util.List;

public record RecommendResponseDto(
        Long userId,
        String userName,
        String profileFile,
        String prevType,
        String bodyType,
        List<RecommendUsersDto> recommendUser //추천 사용자 리스트
){}
