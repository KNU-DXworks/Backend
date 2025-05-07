package project.DxWorks.transaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.transaction.dto.PostTransactionRequestDto;
import project.DxWorks.transaction.dto.TransactionDto;
import project.DxWorks.transaction.service.TransactionDeployService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/transaction")
public class TransactionDeployController {

    private final TransactionDeployService transactionDeployService;

    @PostMapping("/deploy")
    public String deploy(
            @RequestHeader("X-PRIVATE-KEY") String privateKey
    ) {
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

    // ---------- 내 모든 거래 조회 ----------
    @GetMapping
    public List<TransactionDto> getTransactions(
            @RequestHeader("X-PRIVATE-KEY") String privateKey
    ) throws Exception {
        return transactionDeployService.getTransactions(privateKey);
    }

    // ---------- 거래 단건 조회 ----------
    @GetMapping("/detail/{id}")
    public TransactionDto getTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long id
    ) throws Exception {
        return transactionDeployService.getTransaction(privateKey, id);
    }

    // ---------- 거래 수정 ----------
    @PutMapping("/{id}")
    public String updateTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long id,
            @RequestBody PostTransactionRequestDto dto
    ) throws Exception {
        return transactionDeployService.updateTransaction(privateKey, id, dto);
    }

    // ---------- 거래 삭제 ----------
    @DeleteMapping("/{id}")
    public String deleteTransaction(
            @RequestHeader("X-PRIVATE-KEY") String privateKey,
            @PathVariable Long id
    ) throws Exception {
        return transactionDeployService.deleteTransaction(privateKey, id);
    }
}
