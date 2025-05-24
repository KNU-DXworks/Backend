package project.DxWorks.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.transaction.dto.PostTransactionRequestDto;
import project.DxWorks.transaction.dto.TransactionDto;
import project.DxWorks.transaction.dto.response.TransactionResponseDto;
import project.DxWorks.transaction.service.TransactionDeployService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionDeployController {

    private final TransactionDeployService transactionDeployService;

    @PostMapping("/deploy")
    public String deploy(@RequestHeader("X-PRIVATE-KEY") String privateKey) {
        try {
            return transactionDeployService.deployContract(privateKey);
        } catch (Exception e) {
            return "Deploy failed: " + e.getMessage();
        }
    }

    // ---------- 거래 생성 ----------
    @PostMapping
    public String addTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @RequestBody PostTransactionRequestDto dto
    ) throws Exception {
        return transactionDeployService.addTransaction(privateKey, dto);
    }

    // ---------- 거래 송금 ----------
    @PostMapping("/pay/{transactionId}")
    public String payTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long transactionId,
            @RequestParam Long amount
    ) throws Exception {
        return transactionDeployService.payForTransaction(privateKey, transactionId, amount);
    }

    // ---------- 내 모든 거래 조회 ----------
    @GetMapping
    public TransactionResponseDto getTransactions(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @RequestAttribute Long userId
    ) throws Exception {
        return transactionDeployService.getTransactions(privateKey, userId);
    }

    // ---------- 거래 단건 조회 ----------
    @GetMapping("/detail/{transactionId}")
    public TransactionDto getTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long transactionId
    ) throws Exception {
        return transactionDeployService.getTransaction(privateKey, transactionId);
    }

    // ---------- 거래 수정 ----------
    @PutMapping("/{transactionId}")
    public String updateTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long transactionId,
            @RequestBody PostTransactionRequestDto dto
    ) throws Exception {
        return transactionDeployService.updateTransaction(privateKey, transactionId, dto);
    }

    // ---------- 거래 삭제 ----------
    @DeleteMapping("/{transactionId}")
    public String deleteTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long transactionId
    ) throws Exception {
        return transactionDeployService.deleteTransaction(privateKey, transactionId);
    }
}
