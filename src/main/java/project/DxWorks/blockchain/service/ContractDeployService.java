package project.DxWorks.blockchain.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.blockchain.contract.SmartContract;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractDeployService {

    private final Web3j web3j;


    // 스마트컨트랙트 배포
    public String deployContract() throws Exception {
        String privateKey = "0xcf2ce6095228f7bafcd8ae1f9c90ad0fe99d03079cb2cd18232cb25fe3b1f678";
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

    // 스마트 컨트랙트 내용 가져오기
    public String callMessage(String contractAddress) throws Exception {
        String privateKey = "0xcf2ce6095228f7bafcd8ae1f9c90ad0fe99d03079cb2cd18232cb25fe3b1f678";
        Credentials credentials = Credentials.create(privateKey);

        Function function = new Function(
                "message", // 스마트 컨트랙트 함수 이름
                Collections.emptyList(), // 입력 (NULL)
                Collections.singletonList(new TypeReference<Utf8String>() {
                }) // 반환값 String
        );

//        SmartContract smartContract = SmartContract.load(
//                contractAddress,
//                web3j,
//                credentials,
//                new StaticGasProvider(
//                        BigInteger.valueOf(0),
//                        BigInteger.valueOf(0)
//                )
//        );


        // 스마트 컨트랙트는 String으로 이해할 수 없음.
        // 따라서 EVM이 이해할 수 있는 bite code로 변환해야함
        String encoded = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        credentials.getAddress(), // 호출 함수기에 별 의미는 없다
                        contractAddress, // 스마트 컨트랙트가 배포된 주소
                        encoded // 위에서 만든 함수 호출 코드
                ),
                DefaultBlockParameterName.LATEST // 가장 최신의 블록을 조회
        ).send();

//        EthGetCode code = web3j.ethGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
//        System.out.println("▶️ getCode: " + code.getCode());
//
//        System.out.println(response.getValue());
//
//        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
//        return decoded.get(0).getValue().toString();
        System.out.println("▶️ Raw Response: " + response.getValue());
        System.out.println("▶️ hasError: " + response.hasError());
        if (response.hasError()) {
            System.out.println("▶️ Error: " + response.getError().getMessage());
        } else {
            List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            if (!decoded.isEmpty()) {
                System.out.println("✅ message: " + decoded.get(0).getValue().toString());
            } else {
                System.out.println("❌ No result returned.");
            }
        }
        return "hhhh";
    }

    private String readResourceFile(String filename) throws IOException {
        return new String(
                Files.readAllBytes(new ClassPathResource(filename).getFile().toPath()),
                StandardCharsets.UTF_8
        ).replaceAll("\\s+", ""); // 공백 제거 (중요)
    }
}
