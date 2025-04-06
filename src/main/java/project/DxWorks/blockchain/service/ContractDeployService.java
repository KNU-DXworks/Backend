package project.DxWorks.blockchain.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ContractDeployService {

    private final Web3j web3j;

    public String deployContract() throws Exception {
        String privateKey = "0xd9650e4a9bc5631ddf9188625832ec765b1e01fa3f101cfc630f59a9cd8a44b1";
        Credentials credentials = Credentials.create(privateKey);

        // Gas설정
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L), // 20 Gwei
                BigInteger.valueOf(6721975)
        );

        // ABI & BIN 파일 읽기
        String abi = readResourceFile("Hello.abi");
        String bin = readResourceFile("Hello.bin");

        // 트랜잭션 매니저
        RawTransactionManager txManager = new RawTransactionManager(web3j, credentials);

        // 배포
        org.web3j.protocol.core.methods.response.EthSendTransaction response =
                txManager.sendTransaction(
                        gasProvider.getGasPrice(),
                        gasProvider.getGasLimit(),
                        "", // to address = "" for deploy
                        bin,
                        BigInteger.ZERO
                );

        String txHash = response.getTransactionHash();

        // 트랜잭션 receipt 기다리기
        TransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash)
                .send()
                .getTransactionReceipt()
                .orElseThrow(() -> new RuntimeException("No receipt found"));

        return receipt.getContractAddress();
    }

    private String readResourceFile(String filename) throws IOException {
        return new String(
                Files.readAllBytes(new ClassPathResource(filename).getFile().toPath()),
                StandardCharsets.UTF_8
        ).replaceAll("\\s+", ""); // 공백 제거 (중요)
    }
}
