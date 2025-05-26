package project.DxWorks.auth.dto;

public record TelegramUserAccessTokenDto(
        String accessToken,
        String refreshToken,
        long expir,
        boolean walletRegistered,
        String walletAddress) {
}
