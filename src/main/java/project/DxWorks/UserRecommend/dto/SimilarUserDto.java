package project.DxWorks.UserRecommend.dto;

public record SimilarUserDto (
        Long userId,
        Double similarity
)
{};
