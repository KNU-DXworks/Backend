package project.DxWorks.UserRecommend.dto;

public record RecommendUsersDto(
        Long userId,
        String userName,
        String profileImg,
        String prevType,
        String bodyType
) {};
