package project.DxWorks.blockChain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.blockChain.service.WalletService;
import project.DxWorks.user.repository.UserRepository;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createWallet(@RequestAttribute Long userId) {
        Map<String, String> wallet = walletService.createWallet(userId);
        return ResponseEntity.ok(wallet);
    }
}
