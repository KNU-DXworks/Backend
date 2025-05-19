package project.DxWorks.inbody.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.inbody.contract.InbodySmartContract;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.dto.PostInbodyDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractDeployService {

    private final Web3j web3j;

    @Value("${web3.private-key}")
    private String privateKey;

    @Value("${web3.contract.address}")
    private String contractAddress;


    // 스마트컨트랙트 배포
    public String deployContract() throws Exception {
        Credentials credentials = Credentials.create(privateKey);

        // Gas설정
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L), // 20 Gwei
                BigInteger.valueOf(6721975)
        );

        // ABI & BIN 파일 읽기
        String bin = readResourceFile("inbody.bin");

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
        Credentials credentials = Credentials.create(privateKey);

        Function function = new Function(
                "message", // 스마트 컨트랙트 함수 이름
                Collections.emptyList(), // 입력 (NULL)
                Collections.singletonList(new TypeReference<Utf8String>() {
                }) // 반환값 String
        );


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

        return "hhhh";
    }

    public String addInbody(PostInbodyDto requestDto) throws Exception {
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L),
                BigInteger.valueOf(6721975)
        );

        Credentials credentials = Credentials.create(privateKey);
        InbodySmartContract contract = InbodySmartContract.load(contractAddress, web3j, credentials, gasProvider);

        return contract.addInbody(
                requestDto.createdAt(),
                requestDto.gender(),
                BigInteger.valueOf((long)(requestDto.height() * 10)),
                BigInteger.valueOf((long)(requestDto.weight() * 10)),
                BigInteger.valueOf((long)(requestDto.muscle() * 10)),
                BigInteger.valueOf((long)(requestDto.fat() * 10)),
                BigInteger.valueOf((long)(requestDto.bmi() * 10)),
                requestDto.bodyType(),
                requestDto.armGrade(),
                requestDto.bodyGrade(),
                requestDto.legGrade()
        ).send().getTransactionHash();

    }



    // 인바디 정보 가져오기
    public List<InbodyDto> getInbody(String walletAddress) throws IOException {
        // 가스 설정
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L),
                BigInteger.valueOf(6721975)
        );

        // 인증서 생성 및 스마트 컨트랙트 load
        Credentials credentials = Credentials.create(privateKey);
        InbodySmartContract contract = InbodySmartContract.load(contractAddress, web3j, credentials, gasProvider);


        return contract.getMyRecords(web3j,walletAddress,contractAddress);
    }


    private String readResourceFile(String filename) {
        try (InputStream inputStream = new ClassPathResource(filename).getInputStream()) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return content.replaceAll("\\s+", ""); // 공백 제거
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read resource file: " + filename, e);
        }
    }
}
