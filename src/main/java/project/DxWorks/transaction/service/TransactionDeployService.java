package project.DxWorks.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.transaction.contract.TransactionContract;
import project.DxWorks.transaction.dto.PostTransactionRequestDto;
import project.DxWorks.transaction.dto.TransactionDto;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

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

    // ---------- 스마트컨트랙트 배포 ----------
    public String deployContract(String privateKey) throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        ContractGasProvider gasProvider = gasProvider();

        // ABI & BIN 파일 읽기
        String abi = readResourceFile("transaction.abi");
        String bin = readResourceFile("transaction.bin");

        // 트랜잭션 매니저
        RawTransactionManager txManager = new RawTransactionManager(web3j, credentials);

        // 배포
        EthSendTransaction response = txManager.sendTransaction(
                gasProvider.getGasPrice(),
                gasProvider.getGasLimit(),
                "", // to address = "" for deploy
                bin,
                BigInteger.ZERO
        );

        String txHash = response.getTransactionHash();
        if (txHash == null) {
            throw new RuntimeException("트랜잭션 해시가 null입니다. 배포 실패.");
        }

        // receipt 기다리기
        TransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash)
                .send()
                .getTransactionReceipt()
                .orElseThrow(() -> new RuntimeException("트랜잭션 receipt를 찾을 수 없습니다."));

        String deployedAddress = receipt.getContractAddress();
        System.out.println("✅ 컨트랙트 배포 주소: " + deployedAddress);

        return deployedAddress;
    }


    // ---------- 리소스 파일 읽기 (ABI/BIN) ----------
    private String readResourceFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException(fileName + " 파일 읽기 실패: " + e.getMessage(), e);
        }
    }
}
