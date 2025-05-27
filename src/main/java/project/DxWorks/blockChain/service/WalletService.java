package project.DxWorks.blockChain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.dto.response.UserInfResponseDto;
import project.DxWorks.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final Web3j web3j;

    @Value("${web3.private-key}")
    private String serverPrivateKey;

    @Transactional
    public Map<String, String> createWallet(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저의 정보가 없습니다."));

            Profile profile = profileRepository.findByUser(user)
                    .orElseThrow(() -> new IllegalArgumentException("해당유저의 프로필이 존재하지 않습니다."));

            // 키 쌍 생성
            ECKeyPair keyPair = Keys.createEcKeyPair();
            String privateKey = keyPair.getPrivateKey().toString(16);
            String address = "0x" + Keys.getAddress(keyPair.getPublicKey());

            // 결과 반환
            Map<String, String> walletInfo = new HashMap<>();
            walletInfo.put("privateKey", privateKey);
            walletInfo.put("address", address);

            Credentials funderCredentials = Credentials.create(serverPrivateKey);

            Transfer.sendFunds(
                    web3j,
                    funderCredentials,
                    address,
                    BigDecimal.TEN,
                    Convert.Unit.ETHER
            ).send(); // 트랜잭션 완료까지 기다림


            profile.setWalletAddress(address);

            return walletInfo;
        } catch (Exception e) {
            throw new RuntimeException("지갑 생성 실패", e);
        }
    }
}
