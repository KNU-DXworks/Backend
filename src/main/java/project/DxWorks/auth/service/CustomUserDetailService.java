package project.DxWorks.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;
import project.DxWorks.auth.interfacese.UserAuthInterface;
import project.DxWorks.auth.repository.UserAuthRepository;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserAuthInterface userAuthInterface;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // userId를 Long값으로 바꿔주고 인증 정보 조회 (UserDetailsService에서 String으로 오버라이드되게 돼있음)
        UserAuthEntity userAuthEntity = userAuthInterface.findByUserId(Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException("사용자 인증 정보를 찾을 수 없습니다: " + userId));

        // 권한이 필요 없으므로 빈 권한 목록 생성
        List<GrantedAuthority> authorities = Collections.emptyList();

        // Spring Security의 User 객체 생성 (비밀번호는 소셜 로그인이므로 빈 문자열)
        return new User(
                userAuthEntity.getUserId().toString(),
                "", // 소셜 로그인이므로 비밀번호 불필요
                authorities
        );
    }

}
