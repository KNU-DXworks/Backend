package project.DxWorks.fileDeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.fileDeal.contract.FileDealSmartContract;
import project.DxWorks.fileDeal.dto.CreateFileDealRequestDto;
import project.DxWorks.fileDeal.dto.RegisterFileRequestDto;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class FileDealService {

    private final Web3j web3j;

    @Value("${web3.private-key}")
    private String privateKey;

    @Value("${web3.contract.dealAddress}")
    private String contractAddress;

    public String deployContract() throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        // Gas설정
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L), // 20 Gwei
                BigInteger.valueOf(6721975)
        );

        // ABI & BIN 파일 읽기
        String abi = readResourceFile("fileDeal.abi");
        String bin = readResourceFile("fileDeal.bin");

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

    public String createDeal(CreateFileDealRequestDto dto) throws Exception {
        Credentials credentials = Credentials.create(dto.privateKey());
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L),
                BigInteger.valueOf(6721975)
        );

        FileDealSmartContract contract = FileDealSmartContract.load(contractAddress, web3j, credentials, gasProvider);

        BigInteger dealId = contract
                .createDeal(dto.sellerAddress(), dto.amount(), credentials, gasProvider)
                .send();

        return dealId.toString();
    }

    public String registerFile(RegisterFileRequestDto dto) throws Exception {
        Credentials credentials = Credentials.create(dto.privateKey());
        ContractGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(20_000_000_000L), BigInteger.valueOf(6721975));
        FileDealSmartContract contract = FileDealSmartContract.load(contractAddress, web3j, credentials, gasProvider);

        TransactionReceipt receipt = contract.registerFile(dto.dealId(), dto.ipfsHash()).send();
        return receipt.getTransactionHash();
    }

    public String confirmDelivery(BigInteger dealId) throws Exception {
        // NOTE: 실제 구매자의 private key 필요
        Credentials credentials = Credentials.create(privateKey);
        ContractGasProvider gasProvider = new StaticGasProvider(BigInteger.valueOf(20_000_000_000L), BigInteger.valueOf(6721975));
        FileDealSmartContract contract = FileDealSmartContract.load(contractAddress, web3j, credentials, gasProvider);

        TransactionReceipt receipt = contract.confirmDelivery(dealId).send();
        return receipt.getTransactionHash();
    }

    private String readResourceFile(String filename) throws IOException {
        return new String(
                Files.readAllBytes(new ClassPathResource(filename).getFile().toPath()),
                StandardCharsets.UTF_8
        ).replaceAll("\\s+", ""); // 공백 제거 (중요)
    }
}
