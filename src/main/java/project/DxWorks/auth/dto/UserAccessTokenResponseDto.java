package project.DxWorks.auth.dto;

public record UserAccessTokenResponseDto(String accessToken, String refreshToken, long expir) {
}
