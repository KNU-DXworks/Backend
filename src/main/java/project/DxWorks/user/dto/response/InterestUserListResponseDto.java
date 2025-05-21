package project.DxWorks.user.dto.response;

public record InterestUserListResponseDto(
        Long userId,
        String userName,
        String profileImg,
        String prevType,
        String bodyType) {
}
