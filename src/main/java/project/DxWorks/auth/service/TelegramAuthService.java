package project.DxWorks.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.DxWorks.auth.domain.Entity.TelegramAuthEntity;
import project.DxWorks.auth.domain.JwtTokenProvider;
import project.DxWorks.auth.dto.UserAccessTokenResponseDto;
import project.DxWorks.auth.interfacese.UserAuthInterface;
import project.DxWorks.auth.repository.TelegramAuthRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramAuthService {

    @Value("${telegram.bot-token}")
    private String botToken;

    private final JwtTokenProvider jwtTokenProvider;
    private final TelegramAuthRepository telegramAuthRepository;
    private final UserAuthInterface userAuthInterface;
    private final UserDetailsService userDetailsService;

    public UserAccessTokenResponseDto authenticateWithTelegram(String initData) throws JsonProcessingException {
        Map<String, String> data = parseInitData(initData);

        // 1. user 필드 복원
        String userJsonEncoded = data.get("user");
        String userJson = URLDecoder.decode(userJsonEncoded, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userFields = objectMapper.readValue(userJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

        data.remove("user");
        for (Map.Entry<String, Object> entry : userFields.entrySet()) {
            data.put("user." + entry.getKey(), entry.getValue().toString());
        }

        // 2. hash, signature 제거
        String hash = data.remove("hash");
        data.remove("signature");


        // 3. data_check_string 생성
        String dataCheckString = data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));

        // 4. HMAC-SHA256 계산
        byte[] secretKeyBytes = hashSha256Bytes("WebAppData"+ botToken);
        String myHash = hmacSha256(secretKeyBytes, dataCheckString);

        System.out.println("🔍 [DEBUG] dataCheckString:\n" + dataCheckString);
        System.out.println("🔐 [DEBUG] MyHash:   " + myHash);
        System.out.println("🔐 [DEBUG] Telegram: " + hash);


        // 5. 사용자 등록 및 토큰 발급
        Long telegramId = Long.parseLong(data.get("user.id"));
        String firstName = data.getOrDefault("user.first_name", "");
        String lastName = data.getOrDefault("user.last_name", "");
        String username = data.getOrDefault("user.username", "");
        String photoUrl = data.getOrDefault("user.photo_url", "");

        TelegramAuthEntity telegramAuthEntity = telegramAuthRepository.findByTelegramId(telegramId)
                .orElseGet(() -> userAuthInterface.registerTelegramUser(
                        firstName + lastName,
                        telegramId,
                        photoUrl,
                        username));

        UserDetails userDetails = userDetailsService.loadUserByUsername(telegramAuthEntity.getUserId().toString());
        String accessToken = jwtTokenProvider.createAccessToken(userDetails, telegramAuthEntity.getUserId());
        String refreshToken = jwtTokenProvider.createRefreshToken(telegramAuthEntity.getUserId());

        return new UserAccessTokenResponseDto(accessToken, refreshToken, jwtTokenProvider.getTokenValidTime());
    }

    private Map<String, String> parseInitData(String initData) {
        return Arrays.stream(initData.split("&"))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(
                        a -> a[0],
                        a -> a.length > 1 ? a[1] : "",
                        (a, b) -> b,
                        LinkedHashMap::new // ✅ 순서 보장
                ));
    }

    private byte[] hashSha256Bytes(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 error", e);
        }
    }

    private String hmacSha256(byte[] key, String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hmac);
        } catch (Exception e) {
            throw new RuntimeException("HMAC SHA256 error", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
