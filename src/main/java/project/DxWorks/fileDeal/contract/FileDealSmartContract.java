package project.DxWorks.fileDeal.contract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileDealSmartContract extends Contract {

    public static final String BINARY = "0x..."; // 컴파일된 바이트코드

    protected FileDealSmartContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
    }

    public static FileDealSmartContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new FileDealSmartContract(contractAddress, web3j, credentials, gasProvider);
    }

    public RemoteFunctionCall<BigInteger> createDeal(
            String sellerAddress,
            BigInteger amount,
            Credentials credentials,
            ContractGasProvider gasProvider
    ) {
        final Function function = new Function(
                "createDeal",
                Arrays.asList(new Address(sellerAddress)),
                Collections.emptyList() // returns handled manually
        );
        final String senderAddress = credentials.getAddress();

        return new RemoteFunctionCall<>(function, () -> {
            String encodedFunction = FunctionEncoder.encode(function);

            EthSendTransaction transactionResponse = web3j.ethSendTransaction(Transaction.createFunctionCallTransaction(
                    senderAddress,
                    null,
                    gasProvider.getGasPrice("createDeal"),
                    gasProvider.getGasLimit("createDeal"),
                    getContractAddress(),
                    amount,
                    encodedFunction
            )).send();

            String txHash = transactionResponse.getTransactionHash();

            TransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash)
                    .send()
                    .getTransactionReceipt()
                    .orElseThrow(() -> new RuntimeException("No receipt found"));

            // DealCreated 이벤트 파싱
            Event event = new Event("DealCreated",
                    Arrays.asList(
                            new TypeReference<Uint256>(true) {}, // dealId (indexed)
                            new TypeReference<Address>(true) {}, // buyer
                            new TypeReference<Address>(true) {}, // seller
                            new TypeReference<Uint256>() {}      // amount
                    )
            );

            List<EventValuesWithLog> logs = extractEventParametersWithLog(event, receipt);
            if (!logs.isEmpty()) {
                Uint256 dealId = (Uint256) logs.get(0).getIndexedValues().get(0);
                return dealId.getValue();
            }

            throw new RuntimeException("dealId not found in logs");
        });
    }

    public RemoteFunctionCall<TransactionReceipt> registerFile(BigInteger dealId, String ipfsHash) {
        Function function = new Function(
                "registerFile",
                List.of(new Uint256(dealId), new Utf8String(ipfsHash)),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmDelivery(BigInteger dealId) {
        Function function = new Function(
                "confirmDelivery",
                List.of(new Uint256(dealId)),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }
}
