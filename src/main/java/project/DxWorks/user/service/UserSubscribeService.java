package project.DxWorks.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.user.domain.UserSubscribeEntity;
import project.DxWorks.user.repository.UserSubscibeRepository;

@Service
@AllArgsConstructor
public class UserSubscribeService {

    private final ProfileRepository profileRepository;
    private final UserSubscibeRepository subscribeRepository;

    public void subscribeByWalletAddresses(Long transactionId, String buyerAddress, String sellerAddress, int periodDays) {
        UserEntity buyer = profileRepository.findByWalletAddress(buyerAddress)
                .map(Profile::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        UserEntity seller = profileRepository.findByWalletAddress(sellerAddress)
                .map(Profile::getUser)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        // 중복 구독 방지
        if (!subscribeRepository.existsByFromUserAndToUser(buyer, seller)) {
            UserSubscribeEntity subscribe = new UserSubscribeEntity(transactionId, buyer, seller, periodDays);
            subscribeRepository.save(subscribe);
        }
    }
}
