package project.DxWorks.UserRecommend.dto;



public record RecommendResponseDto(
    long userId,
    String userName,
    String profileFile,
    String prevType,
    String bodyType
){}
