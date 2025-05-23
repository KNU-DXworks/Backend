package project.DxWorks.transaction.contract;

import org.web3j.abi.EventValues;
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
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import project.DxWorks.transaction.dto.TransactionDto;
import project.DxWorks.transaction.struct.TransactionStruct;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class TransactionContract extends Contract {

    public static final String BINARY = "608060405260015f55348015610013575f80fd5b50611889806100215f395ff3fe608060405234801561000f575f80fd5b506004361061007a575f3560e01c806388fa40691161005957806388fa4069146100ed5780639ace38c214610109578063c3d68d431461013e578063d7ca38f31461015a5761007a565b8062e9c0061461007e57806333ea3dc81461009a57806383920e90146100cf575b5f80fd5b61009860048036038101906100939190610cb3565b610178565b005b6100b460048036038101906100af9190610cb3565b610292565b6040516100c696959493929190610db6565b60405180910390f35b6100d76103a7565b6040516100e49190610fc3565b60405180910390f35b61010760048036038101906101029190611044565b61063c565b005b610123600480360381019061011e9190610cb3565b610705565b60405161013596959493929190610db6565b60405180910390f35b610158600480360381019061015391906110f2565b610801565b005b610162610a58565b60405161016f9190611176565b60405180910390f35b5f60015f8381526020019081526020015f20905061019581610a5d565b6101d4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101cb906111ff565b60405180910390fd5b60015f8381526020019081526020015f205f8082015f9055600182015f6101000a81549073ffffffffffffffffffffffffffffffffffffffff0219169055600282015f9055600382015f9055600482015f61022f9190610bc3565b600582015f6101000a81549073ffffffffffffffffffffffffffffffffffffffff021916905550507f4557095eb267dfd5f7e33f676e26d820f8221c17988aea42c6776b36f31f06a0826040516102869190611176565b60405180910390a15050565b5f805f8060605f8060015f8981526020019081526020015f209050805f0154816001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff168260020154836003015484600401856005015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff168180546103139061124a565b80601f016020809104026020016040519081016040528092919081815260200182805461033f9061124a565b801561038a5780601f106103615761010080835404028352916020019161038a565b820191905f5260205f20905b81548152906001019060200180831161036d57829003601f168201915b505050505091509650965096509650965096505091939550919395565b60605f80600190505b5f548110156103f9576103d260015f8381526020019081526020015f20610b10565b156103e65781806103e2906112a7565b9250505b80806103f1906112a7565b9150506103b0565b505f8167ffffffffffffffff811115610415576104146112ee565b5b60405190808252806020026020018201604052801561044e57816020015b61043b610c00565b8152602001906001900390816104335790505b5090505f80600190505b5f548110156106325761047a60015f8381526020019081526020015f20610b10565b1561061f5760015f8281526020019081526020015f206040518060c00160405290815f8201548152602001600182015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820154815260200160048201805461051d9061124a565b80601f01602080910402602001604051908101604052809291908181526020018280546105499061124a565b80156105945780601f1061056b57610100808354040283529160200191610594565b820191905f5260205f20905b81548152906001019060200180831161057757829003601f168201915b50505050508152602001600582015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815250508383815181106106055761060461131b565b5b6020026020010181905250818061061b906112a7565b9250505b808061062a906112a7565b915050610458565b5081935050505090565b5f60015f8781526020019081526020015f20905061065981610a5d565b610698576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161068f906113b8565b60405180910390fd5b84816002018190555083816003018190555082828260040191826106bd92919061157d565b507f62a5cb17bd0bdb4655ed5e60656f59f26b74f6f8c3c916a48b6e968cdbb5e5f186868686866040516106f5959493929190611684565b60405180910390a1505050505050565b6001602052805f5260405f205f91509050805f015490806001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169080600201549080600301549080600401805461075b9061124a565b80601f01602080910402602001604051908101604052809291908181526020018280546107879061124a565b80156107d25780601f106107a9576101008083540402835291602001916107d2565b820191905f5260205f20905b8154815290600101906020018083116107b557829003601f168201915b505050505090806005015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905086565b5f73ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff160361086f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108669061171a565b60405180910390fd5b5f6040518060c001604052805f5481526020018773ffffffffffffffffffffffffffffffffffffffff16815260200186815260200185815260200184848080601f0160208091040260200160405190810160405280939291908181526020018383808284375f81840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff1681525090508060015f805481526020019081526020015f205f820151815f01556020820151816001015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550604082015181600201556060820151816003015560808201518160040190816109999190611738565b5060a0820151816005015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055509050508573ffffffffffffffffffffffffffffffffffffffff165f547e3e6f8150252454cffc86a370622ad98e102cf98615a96391843f7790d289ef8787878733604051610a32959493929190611807565b60405180910390a35f80815480929190610a4b906112a7565b9190505550505050505050565b5f5481565b5f816005015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161480610b095750816001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b9050919050565b5f3373ffffffffffffffffffffffffffffffffffffffff16826005015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161480610bbc57503373ffffffffffffffffffffffffffffffffffffffff16826001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16145b9050919050565b508054610bcf9061124a565b5f825580601f10610be05750610bfd565b601f0160209004905f5260205f2090810190610bfc9190610c5d565b5b50565b6040518060c001604052805f81526020015f73ffffffffffffffffffffffffffffffffffffffff1681526020015f81526020015f8152602001606081526020015f73ffffffffffffffffffffffffffffffffffffffff1681525090565b5b80821115610c74575f815f905550600101610c5e565b5090565b5f80fd5b5f80fd5b5f819050919050565b610c9281610c80565b8114610c9c575f80fd5b50565b5f81359050610cad81610c89565b92915050565b5f60208284031215610cc857610cc7610c78565b5b5f610cd584828501610c9f565b91505092915050565b610ce781610c80565b82525050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f610d1682610ced565b9050919050565b610d2681610d0c565b82525050565b5f81519050919050565b5f82825260208201905092915050565b5f5b83811015610d63578082015181840152602081019050610d48565b5f8484015250505050565b5f601f19601f8301169050919050565b5f610d8882610d2c565b610d928185610d36565b9350610da2818560208601610d46565b610dab81610d6e565b840191505092915050565b5f60c082019050610dc95f830189610cde565b610dd66020830188610d1d565b610de36040830187610cde565b610df06060830186610cde565b8181036080830152610e028185610d7e565b9050610e1160a0830184610d1d565b979650505050505050565b5f81519050919050565b5f82825260208201905092915050565b5f819050602082019050919050565b610e4e81610c80565b82525050565b610e5d81610d0c565b82525050565b5f82825260208201905092915050565b5f610e7d82610d2c565b610e878185610e63565b9350610e97818560208601610d46565b610ea081610d6e565b840191505092915050565b5f60c083015f830151610ec05f860182610e45565b506020830151610ed36020860182610e54565b506040830151610ee66040860182610e45565b506060830151610ef96060860182610e45565b5060808301518482036080860152610f118282610e73565b91505060a0830151610f2660a0860182610e54565b508091505092915050565b5f610f3c8383610eab565b905092915050565b5f602082019050919050565b5f610f5a82610e1c565b610f648185610e26565b935083602082028501610f7685610e36565b805f5b85811015610fb15784840389528151610f928582610f31565b9450610f9d83610f44565b925060208a01995050600181019050610f79565b50829750879550505050505092915050565b5f6020820190508181035f830152610fdb8184610f50565b905092915050565b5f80fd5b5f80fd5b5f80fd5b5f8083601f84011261100457611003610fe3565b5b8235905067ffffffffffffffff81111561102157611020610fe7565b5b60208301915083600182028301111561103d5761103c610feb565b5b9250929050565b5f805f805f6080868803121561105d5761105c610c78565b5b5f61106a88828901610c9f565b955050602061107b88828901610c9f565b945050604061108c88828901610c9f565b935050606086013567ffffffffffffffff8111156110ad576110ac610c7c565b5b6110b988828901610fef565b92509250509295509295909350565b6110d181610d0c565b81146110db575f80fd5b50565b5f813590506110ec816110c8565b92915050565b5f805f805f6080868803121561110b5761110a610c78565b5b5f611118888289016110de565b955050602061112988828901610c9f565b945050604061113a88828901610c9f565b935050606086013567ffffffffffffffff81111561115b5761115a610c7c565b5b61116788828901610fef565b92509250509295509295909350565b5f6020820190506111895f830184610cde565b92915050565b7f4163636573732064656e6965643a206f6e6c79206f776e65722063616e2064655f8201527f6c65746500000000000000000000000000000000000000000000000000000000602082015250565b5f6111e9602483610d36565b91506111f48261118f565b604082019050919050565b5f6020820190508181035f830152611216816111dd565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061126157607f821691505b6020821081036112745761127361121d565b5b50919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6112b182610c80565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036112e3576112e261127a565b5b600182019050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4163636573732064656e6965643a206f6e6c79206f776e65722063616e2075705f8201527f6461746500000000000000000000000000000000000000000000000000000000602082015250565b5f6113a2602483610d36565b91506113ad82611348565b604082019050919050565b5f6020820190508181035f8301526113cf81611396565b9050919050565b5f82905092915050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f6008830261143c7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82611401565b6114468683611401565b95508019841693508086168417925050509392505050565b5f819050919050565b5f61148161147c61147784610c80565b61145e565b610c80565b9050919050565b5f819050919050565b61149a83611467565b6114ae6114a682611488565b84845461140d565b825550505050565b5f90565b6114c26114b6565b6114cd818484611491565b505050565b5b818110156114f0576114e55f826114ba565b6001810190506114d3565b5050565b601f82111561153557611506816113e0565b61150f846113f2565b8101602085101561151e578190505b61153261152a856113f2565b8301826114d2565b50505b505050565b5f82821c905092915050565b5f6115555f198460080261153a565b1980831691505092915050565b5f61156d8383611546565b9150826002028217905092915050565b61158783836113d6565b67ffffffffffffffff8111156115a05761159f6112ee565b5b6115aa825461124a565b6115b58282856114f4565b5f601f8311600181146115e2575f84156115d0578287013590505b6115da8582611562565b865550611641565b601f1984166115f0866113e0565b5f5b82811015611617578489013582556001820191506020850194506020810190506115f2565b868310156116345784890135611630601f891682611546565b8355505b6001600288020188555050505b50505050505050565b828183375f83830152505050565b5f6116638385610d36565b935061167083858461164a565b61167983610d6e565b840190509392505050565b5f6080820190506116975f830188610cde565b6116a46020830187610cde565b6116b16040830186610cde565b81810360608301526116c4818486611658565b90509695505050505050565b7f54726164657220616464726573732063616e6e6f74206265207a65726f0000005f82015250565b5f611704601d83610d36565b915061170f826116d0565b602082019050919050565b5f6020820190508181035f830152611731816116f8565b9050919050565b61174182610d2c565b67ffffffffffffffff81111561175a576117596112ee565b5b611764825461124a565b61176f8282856114f4565b5f60209050601f8311600181146117a0575f841561178e578287015190505b6117988582611562565b8655506117ff565b601f1984166117ae866113e0565b5f5b828110156117d5578489015182556001820191506020850194506020810190506117b0565b868310156117f257848901516117ee601f891682611546565b8355505b6001600288020188555050505b505050505050565b5f60808201905061181a5f830188610cde565b6118276020830187610cde565b818103604083015261183a818587611658565b90506118496060830184610d1d565b969550505050505056fea2646970667358221220a1b0529c5ffc10ca2d10c969587f6e524d513661ea219cf129b0b78e6266100f64736f6c63430008140033";

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

    //    // ---------- 거래 생성 ----------
//    public RemoteFunctionCall<TransactionReceipt> createTransaction(
//            String traderId,
//            BigInteger transactionPeriod,
//            BigInteger amount,
//            String info
//    ) {
//        Function function = new Function(
//                "createTransaction",
//                Arrays.asList(
//                        new Address(traderId),
//                        new Uint256(transactionPeriod),
//                        new Uint256(amount),
//                        new Utf8String(info)
//                ),
//                Collections.emptyList()
//        );
//        return executeRemoteCallTransaction(function);
//    }
//
//    public Optional<BigInteger> getCreatedTransactionId(TransactionReceipt receipt) {
//        final Event event = new Event("TransactionCreated",
//                Arrays.asList(
//                        new TypeReference<Uint256>(true) {},     // indexed id
//                        new TypeReference<Address>(true) {},     // indexed traderId
//                        new TypeReference<Uint256>() {},         // transactionPeriod
//                        new TypeReference<Uint256>() {},         // amount
//                        new TypeReference<Utf8String>() {},      // info
//                        new TypeReference<Address>() {}          // creator
//                )
//        );
//
//        for (Log log : receipt.getLogs()) {
//            EventValuesWithLog eventValues = staticExtractEventParametersWithLog(event, log);
//            if (eventValues != null) {
//                Uint256 id = (Uint256) eventValues.getIndexedValues().get(0);
//                return Optional.of(id.getValue());
//            }
//        }
//
//        return Optional.empty();
//    }



    // ---------- 거래 생성 ----------
    public RemoteFunctionCall<TransactionReceipt> createTransaction(
            String traderId,
            BigInteger transactionPeriod,
            BigInteger amount,
            String info
    ) {
        Function function = new Function(
                "createTransaction",
                Arrays.asList(
                        new Address(traderId),
                        new Uint256(transactionPeriod),
                        new Uint256(amount),
                        new Utf8String(info)
                ),
                Collections.emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    public Optional<BigInteger> getCreatedTransactionId(TransactionReceipt receipt) {
        final Event event = new Event("TransactionCreated", Arrays.<TypeReference<?>>asList(
                new TypeReference<Uint256>(true) {},     // indexed id
                new TypeReference<Address>(true) {},     // indexed traderId
                new TypeReference<Uint256>() {},         // transactionPeriod
                new TypeReference<Uint256>() {},         // amount
                new TypeReference<Utf8String>() {},      // info
                new TypeReference<Address>() {}          // creator
        ));

        for (Log log : receipt.getLogs()) {
            EventValues eventValues = Contract.staticExtractEventParameters(event, log);
            if (eventValues != null && !eventValues.getIndexedValues().isEmpty()) {
                Uint256 id = (Uint256) eventValues.getIndexedValues().get(0);
                return Optional.of(id.getValue());
            }
        }

        return Optional.empty();
    }








    //    // ---------- 단건 조회 ----------
//    public TransactionDto getTransaction(BigInteger transactionId) throws Exception {
//        Function function = new Function(
//                "getTransaction",
//                Collections.singletonList(new Uint256(transactionId)),
//                Arrays.asList(
//                        new TypeReference<Uint256>() {},
//                        new TypeReference<Uint8>() {},
//                        new TypeReference<Address>() {},
//                        new TypeReference<Utf8String>() {},
//                        new TypeReference<Uint256>() {},
//                        new TypeReference<Utf8String>() {},
//                        new TypeReference<Address>() {}
//                )
//        );
//
//        String encoded = FunctionEncoder.encode(function);
//
//        EthCall response = web3j.ethCall(
//                Transaction.createEthCallTransaction(
//                        credentials.getAddress(), getContractAddress(), encoded),
//                DefaultBlockParameterName.LATEST
//        ).send();
//
//        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
//
//        if (decoded.isEmpty()) {
//            throw new RuntimeException("No transaction found");
//        }
//
//        return new TransactionDto(
//                ((BigInteger) decoded.get(0).getValue()).longValue(),
//                decoded.get(1).getValue().toString(),
//                ((BigInteger) decoded.get(2).getValue()).intValue(),
//                ((BigInteger) decoded.get(3).getValue()).longValue(),
//                decoded.get(4).getValue().toString(),
//                decoded.get(5).getValue().toString()
//        );
//    }

    public TransactionDto getTransaction(BigInteger transactionId) throws Exception {
        Function function = new Function(
                "getTransaction",
                Collections.singletonList(new Uint256(transactionId)),
                Arrays.asList(
                        new TypeReference<Uint256>() {},   // id
                        new TypeReference<Address>() {},   // traderId
                        new TypeReference<Uint256>() {},   // transactionPeriod
                        new TypeReference<Uint256>() {},   // amount
                        new TypeReference<Utf8String>() {},// info
                        new TypeReference<Address>() {}    // creator
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
                ((BigInteger) decoded.get(0).getValue()).longValue(),    // id
                decoded.get(1).getValue().toString(),                    // traderId
                ((BigInteger) decoded.get(2).getValue()).intValue(),     // transactionPeriod
                ((BigInteger) decoded.get(3).getValue()).longValue(),    // amount
                decoded.get(4).getValue().toString(),                    // info
                decoded.get(5).getValue().toString()                     // creator
        );
    }

    // ---------- 거래 수정 ----------
    public RemoteFunctionCall<TransactionReceipt> updateTransaction(
            BigInteger transactionId,
            BigInteger transactionPeriod,
            BigInteger amount,
            String info
    ) {
        Function function = new Function(
                "updateTransaction",
                Arrays.asList(
                        new Uint256(transactionId),
                        new Uint256(transactionPeriod),
                        new Uint256(amount),
                        new Utf8String(info)
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
                        ((BigInteger) fields.get(0).getValue()).longValue(),       // id
                        fields.get(1).getValue().toString(),                       // traderId
                        ((BigInteger) fields.get(2).getValue()).intValue(),        // transactionPeriod
                        ((BigInteger) fields.get(3).getValue()).longValue(),       // amount
                        fields.get(4).getValue().toString(),                       // info
                        fields.get(5).getValue().toString()                        // creator                     // creator
                );

                result.add(dto);
            }
        }

        return result;
    }
}

