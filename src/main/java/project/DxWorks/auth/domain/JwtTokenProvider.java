package project.DxWorks.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private static final long ACCESS_TOKEN_TIME = 1000L * 60 * 60;
    private static final long REFRESH_TOKEN_TIME = 1000L * 60 * 60 * 24 * 7;

    public JwtTokenProvider(@Value("${secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(UserDetails userDetails, Long userId){
        Claims claims = Jwts.claims()
                .add("username", userDetails.getUsername())
                .add("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_TIME);

        return Jwts.builder()
                .subject(userId.toString())
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId){
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_TIME);

        return Jwts.builder()
                .subject(userId.toString()) // userId를 subject로 설정
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = Long.parseLong(claims.getSubject());
        List<String> authorities = claims.get("authorities", List.class);

        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new User(userId.toString(), "", grantedAuthorities);

        return new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorities);
    }

    public Long getUserId(String token) {
        return Long.parseLong(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject()
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getTokenValidTime() {
        return ACCESS_TOKEN_TIME;
    }
}
