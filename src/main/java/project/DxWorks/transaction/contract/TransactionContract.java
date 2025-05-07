package project.DxWorks.transaction.contract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import project.DxWorks.transaction.dto.TransactionDto;
import project.DxWorks.transaction.struct.TransactionStruct;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class TransactionContract extends Contract {

    public static final String BINARY = "/* 배포된 바이트코드 넣기 */";

    private final Web3j web3j;
    private final Credentials credentials;

    protected TransactionContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public static TransactionContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new TransactionContract(contractAddress, web3j, credentials, gasProvider);
    }

    // ---------- 거래 생성 ----------
    public RemoteFunctionCall<TransactionReceipt> createTransaction(
            BigInteger transactionType,
            String trader,
            String transactionCycle,
            BigInteger transactionPrice,
            String transactionDescription
    ) {
        Function function = new Function(
                "createTransaction",
                Arrays.asList(
                        new Uint8(transactionType),
                        new Address(trader),
                        new Utf8String(transactionCycle),
                        new Uint256(transactionPrice),
                        new Utf8String(transactionDescription)
                ),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    // ---------- 단건 조회 ----------
    public TransactionDto getTransaction(Long transactionId) throws Exception {
        Function function = new Function(
                "getTransaction",
                Collections.singletonList(new Uint256(BigInteger.valueOf(transactionId))),
                Arrays.asList(
                        new TypeReference<Uint256>() {},
                        new TypeReference<Uint8>() {},
                        new TypeReference<Address>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Uint256>() {},
                        new TypeReference<Utf8String>() {},
                        new TypeReference<Address>() {}
                )
        );

        String encoded = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        credentials.getAddress(), getContractAddress(), encoded),
                DefaultBlockParameterName.LATEST
        ).send();

        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

        if (decoded.isEmpty()) {
            throw new RuntimeException("No transaction found");
        }

        return new TransactionDto(
                ((BigInteger) decoded.get(0).getValue()).longValue(),
                ((BigInteger) decoded.get(1).getValue()).intValue(),
                decoded.get(2).getValue().toString(),
                decoded.get(3).getValue().toString(),
                ((BigInteger) decoded.get(4).getValue()).longValue(),
                decoded.get(5).getValue().toString(),
                decoded.get(6).getValue().toString()
        );
    }

    // ---------- 거래 수정 ----------
    public RemoteFunctionCall<TransactionReceipt> updateTransaction(
            BigInteger transactionId,
            BigInteger transactionType,
            String transactionCycle,
            BigInteger transactionPrice,
            String transactionDescription
    ) {
        Function function = new Function(
                "updateTransaction",
                Arrays.asList(
                        new Uint256(transactionId),
                        new Uint8(transactionType),
                        new Utf8String(transactionCycle),
                        new Uint256(transactionPrice),
                        new Utf8String(transactionDescription)
                ),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    // ---------- 거래 삭제 ----------
    public RemoteFunctionCall<TransactionReceipt> deleteTransaction(BigInteger transactionId) {
        Function function = new Function(
                "deleteTransaction",
                Collections.singletonList(new Uint256(transactionId)),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    // ---------- 내 거래 전체 조회 ----------
    public List<TransactionDto> getTransactions() throws IOException {

        Function function = new Function(
                "getTransactions",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<DynamicArray<TransactionStruct>>() {})
        );

        String encoded = FunctionEncoder.encode(function);

        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        credentials.getAddress(), getContractAddress(), encoded),
                DefaultBlockParameterName.LATEST
        ).send();

        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

        List<TransactionDto> result = new ArrayList<>();

        if (!decoded.isEmpty()) {
            List<DynamicStruct> records = (List<DynamicStruct>) decoded.get(0).getValue();

            for (DynamicStruct record : records) {
                List<Type> fields = record.getValue();

                TransactionDto dto = new TransactionDto(
                        ((BigInteger) fields.get(0).getValue()).longValue(),
                        ((BigInteger) fields.get(1).getValue()).intValue(),
                        fields.get(2).getValue().toString(),
                        fields.get(3).getValue().toString(),
                        ((BigInteger) fields.get(4).getValue()).longValue(),
                        fields.get(5).getValue().toString(),
                        fields.get(6).getValue().toString()
                );

                result.add(dto);
            }
        }

        return result;
    }
}

