package project.DxWorks.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.transaction.contract.TransactionContract;
import project.DxWorks.transaction.dto.PostTransactionRequestDto;
import project.DxWorks.transaction.dto.TransactionDto;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDeployService {

    private final Web3j web3j;

    @Value("${web3.contract.transactionAddress}")
    private String contractAddress;

    // ---------- GAS PROVIDER ----------
    private ContractGasProvider gasProvider() {
        return new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L), // 20 Gwei
                BigInteger.valueOf(6721975)
        );
    }

    // ---------- CONTRACT INSTANCE ----------
    private TransactionContract loadContract(String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        return TransactionContract.load(contractAddress, web3j, credentials, gasProvider());
    }

    // ---------- 거래 생성 ----------
    public String addTransaction(String privateKey, PostTransactionRequestDto dto) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.createTransaction(
                BigInteger.valueOf(dto.getTransactionType()),
                dto.getTrader(),
                dto.getTransactionCycle(),
                BigInteger.valueOf(dto.getTransactionPrice()),
                dto.getTransactionDescription()
        ).send().getTransactionHash();
    }

    // ---------- 거래 단건 조회 ----------
    public TransactionDto getTransaction(String privateKey, Long transactionId) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.getTransaction(transactionId);
    }

    // ---------- 거래 수정 ----------
    public String updateTransaction(String privateKey, Long transactionId, PostTransactionRequestDto dto) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.updateTransaction(
                BigInteger.valueOf(transactionId),
                BigInteger.valueOf(dto.getTransactionType()),
                dto.getTransactionCycle(),
                BigInteger.valueOf(dto.getTransactionPrice()),
                dto.getTransactionDescription()
        ).send().getTransactionHash();
    }

    // ---------- 거래 삭제 ----------
    public String deleteTransaction(String privateKey, Long transactionId) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.deleteTransaction(BigInteger.valueOf(transactionId)).send().getTransactionHash();
    }

    // ---------- 내 거래 전체 조회 ----------
    public List<TransactionDto> getTransactions(String privateKey) throws IOException {
        TransactionContract contract = loadContract(privateKey);
        return contract.getTransactions();
    }
}
