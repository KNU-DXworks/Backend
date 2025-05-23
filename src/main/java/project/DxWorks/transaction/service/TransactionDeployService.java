package project.DxWorks.transaction.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.transaction.contract.TransactionContract;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.transaction.dto.PostTransactionRequestDto;
import project.DxWorks.transaction.dto.TransactionDto;
import project.DxWorks.transaction.dto.response.TransactionObjectDto;
import project.DxWorks.transaction.dto.response.TransactionResponseDto;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;
import project.DxWorks.user.dto.response.mainpage.SubscribeUserDto;
import project.DxWorks.user.repository.UserSubscibeRepository;
import project.DxWorks.user.service.UserSubscribeService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class TransactionDeployService {

    private final UserSubscribeService subscribeService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserSubscibeRepository userSubscibeRepository;

    private static final Logger log = LoggerFactory.getLogger(TransactionDeployService.class);

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

        TransactionReceipt receipt = contract.createTransaction(
                dto.getBuyerId(),
                BigInteger.valueOf(dto.getTransactionPeriod()),
                BigInteger.valueOf(dto.getAmount()),
                dto.getInfo()
        ).send();

        contract.getCreatedTransactionId(receipt).ifPresent(id -> log.info("✅ emit된 거래 ID: {}", id));
        return receipt.getTransactionHash();
    }

    // ---------- 거래 송금 ----------
    public String payForTransaction(String privateKey, Long transactionId, Long amount) throws Exception {
        TransactionContract contract = loadContract(privateKey);

        // 1. 송금 트랜잭션 실행
        TransactionReceipt receipt = contract.payForTransaction(
                BigInteger.valueOf(transactionId),
                BigInteger.valueOf(amount)
        ).send();

        // 2. 거래 정보 조회
        TransactionDto tx = contract.getTransaction(BigInteger.valueOf(transactionId));

        System.out.println("tx.getBuyer() = " + tx.getBuyer());
        System.out.println("tx.getSeller() = " + tx.getSeller());
        System.out.println("tx.getTransactionPeriod() = " + tx.getTransactionPeriod());
        // 3. 구독 처리
        subscribeService.subscribeByWalletAddresses(
                tx.getId(),
                tx.getBuyer(),
                tx.getSeller(),
                tx.getTransactionPeriod()
        );


        return receipt.getTransactionHash();

    }

    // ---------- 거래 단건 조회 ----------
    public TransactionDto getTransaction(String privateKey, Long transactionId) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.getTransaction(BigInteger.valueOf(transactionId));
    }

    // ---------- 거래 수정 ----------
    public String updateTransaction(String privateKey, Long transactionId, PostTransactionRequestDto dto) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.updateTransaction(
                BigInteger.valueOf(transactionId),
                BigInteger.valueOf(dto.getTransactionPeriod()),
                BigInteger.valueOf(dto.getAmount()),
                dto.getInfo()
        ).send().getTransactionHash();
    }

    // ---------- 거래 삭제 ----------
    public String deleteTransaction(String privateKey, Long transactionId) throws Exception {
        TransactionContract contract = loadContract(privateKey);
        return contract.deleteTransaction(BigInteger.valueOf(transactionId)).send().getTransactionHash();
    }

    // ---------- 내 거래 전체 조회 ----------
    public TransactionResponseDto getTransactions(String privateKey, Long userId) throws IOException {

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 사용자입니다."));

        String wallet = profileRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException(("잘못된 프로필입니다.")))
                .getWalletAddress().toLowerCase();

        System.out.println("wallet = " + wallet);


        TransactionContract contract = loadContract(privateKey);

        // 리스트 받아오기
        List<TransactionDto> list = contract.getTransactions();

        System.out.println("list = " + list);

        List<TransactionObjectDto> followers = list.stream()
                .filter(dto -> dto.getSeller().equals(wallet))
                .map(dto -> {
                    Profile profile = profileRepository.findByWalletAddress(dto.getBuyer())
                            .orElseThrow(()-> new IllegalArgumentException("구매자 정보가 없습니다."));
                    UserEntity trader = profile.getUser();

                    String expirationDate;
                    if (dto.isPaid()) {
                        expirationDate = userSubscibeRepository.findByTransactionId(dto.getId())
                                .map(sub -> sub.getExpiresAt().toString())
                                .orElse("만료일 없음"); // 예외 처리
                    } else {
                        expirationDate = "미체결 거래입니다";
                    }

                    return new TransactionObjectDto(
                            dto.getId(),
                            trader.getId(),
                            trader.getUserName(),
                            profile.getProfileUrl(),
                            profile.getWalletAddress(),
                            dto.getTransactionPeriod(),
                            dto.getAmount(),
                            dto.isPaid(),
                            dto.getCreatedAt(),
                            expirationDate
                    );
                })
                .toList();

        List<TransactionObjectDto> followings = list.stream()
                .filter(dto -> dto.getBuyer().equals(wallet))
                .map(dto -> {
                    Profile profile = profileRepository.findByWalletAddress(dto.getSeller())
                            .orElseThrow(()-> new IllegalArgumentException("구매자 정보가 없습니다."));
                    UserEntity trader = profile.getUser();

                    String expirationDate;
                    if (dto.isPaid()) {
                        expirationDate = userSubscibeRepository.findByTransactionId(dto.getId())
                                .map(sub -> sub.getExpiresAt().toString())
                                .orElse("만료일 없음"); // 예외 처리
                    } else {
                        expirationDate = "미체결 거래입니다";
                    }

                    return new TransactionObjectDto(
                            dto.getId(),
                            trader.getId(),
                            trader.getUserName(),
                            profile.getProfileUrl(),
                            profile.getWalletAddress(),
                            dto.getTransactionPeriod(),
                            dto.getAmount(),
                            dto.isPaid(),
                            dto.getCreatedAt(),
                            expirationDate
                    );
                })
                .toList();

        TransactionResponseDto dto = new TransactionResponseDto(followings, followers);

        return dto;
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
