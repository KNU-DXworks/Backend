package project.DxWorks.inbody.contract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
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
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.struct.InbodyStruct;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmartContract extends Contract {
    public static final String BINARY = "60806040526040518060400160405280601281526020017f48656c6c6f2c20426c6f636b636861696e2100000000000000000000000000008152505f90816100479190610293565b50348015610053575f80fd5b50610362565b5f81519050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f60028204905060018216806100d457607f821691505b6020821081036100e7576100e6610090565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026101497fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8261010e565b610153868361010e565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f61019761019261018d8461016b565b610174565b61016b565b9050919050565b5f819050919050565b6101b08361017d565b6101c46101bc8261019e565b84845461011a565b825550505050565b5f90565b6101d86101cc565b6101e38184846101a7565b505050565b5b81811015610206576101fb5f826101d0565b6001810190506101e9565b5050565b601f82111561024b5761021c816100ed565b610225846100ff565b81016020851015610234578190505b610248610240856100ff565b8301826101e8565b50505b505050565b5f82821c905092915050565b5f61026b5f1984600802610250565b1980831691505092915050565b5f610283838361025c565b9150826002028217905092915050565b61029c82610059565b67ffffffffffffffff8111156102b5576102b4610063565b5b6102bf82546100bd565b6102ca82828561020a565b5f60209050601f8311600181146102fb575f84156102e9578287015190505b6102f38582610278565b86555061035a565b601f198416610309866100ed565b5f5b828110156103305784890151825560018201915060208501945060208101905061030b565b8683101561034d5784890151610349601f89168261025c565b8355505b6001600288020188555050505b505050505050565b61062b8061036f5f395ff3fe608060405234801561000f575f80fd5b5060043610610034575f3560e01c8063368b877214610038578063e21f37ce14610054575b5f80fd5b610052600480360381019061004d919061025c565b610072565b005b61005c610084565b6040516100699190610303565b60405180910390f35b805f90816100809190610526565b5050565b5f805461009090610350565b80601f01602080910402602001604051908101604052809291908181526020018280546100bc90610350565b80156101075780601f106100de57610100808354040283529160200191610107565b820191905f5260205f20905b8154815290600101906020018083116100ea57829003601f168201915b505050505081565b5f604051905090565b5f80fd5b5f80fd5b5f80fd5b5f80fd5b5f601f19601f8301169050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52604160045260245ffd5b61016e82610128565b810181811067ffffffffffffffff8211171561018d5761018c610138565b5b80604052505050565b5f61019f61010f565b90506101ab8282610165565b919050565b5f67ffffffffffffffff8211156101ca576101c9610138565b5b6101d382610128565b9050602081019050919050565b828183375f83830152505050565b5f6102006101fb846101b0565b610196565b90508281526020810184848401111561021c5761021b610124565b5b6102278482856101e0565b509392505050565b5f82601f83011261024357610242610120565b5b81356102538482602086016101ee565b91505092915050565b5f6020828403121561027157610270610118565b5b5f82013567ffffffffffffffff81111561028e5761028d61011c565b5b61029a8482850161022f565b91505092915050565b5f81519050919050565b5f82825260208201905092915050565b8281835e5f83830152505050565b5f6102d5826102a3565b6102df81856102ad565b93506102ef8185602086016102bd565b6102f881610128565b840191505092915050565b5f6020820190508181035f83015261031b81846102cb565b905092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602260045260245ffd5b5f600282049050600182168061036757607f821691505b60208210810361037a57610379610323565b5b50919050565b5f819050815f5260205f209050919050565b5f6020601f8301049050919050565b5f82821b905092915050565b5f600883026103dc7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff826103a1565b6103e686836103a1565b95508019841693508086168417925050509392505050565b5f819050919050565b5f819050919050565b5f61042a610425610420846103fe565b610407565b6103fe565b9050919050565b5f819050919050565b61044383610410565b61045761044f82610431565b8484546103ad565b825550505050565b5f90565b61046b61045f565b61047681848461043a565b505050565b5b818110156104995761048e5f82610463565b60018101905061047c565b5050565b601f8211156104de576104af81610380565b6104b884610392565b810160208510156104c7578190505b6104db6104d385610392565b83018261047b565b50505b505050565b5f82821c905092915050565b5f6104fe5f19846008026104e3565b1980831691505092915050565b5f61051683836104ef565b9150826002028217905092915050565b61052f826102a3565b67ffffffffffffffff81111561054857610547610138565b5b6105528254610350565b61055d82828561049d565b5f60209050601f83116001811461058e575f841561057c578287015190505b610586858261050b565b8655506105ed565b601f19841661059c86610380565b5f5b828110156105c35784890151825560018201915060208501945060208101905061059e565b868310156105e057848901516105dc601f8916826104ef565b8355505b6001600288020188555050505b50505050505056fea26469706673582212207adcc4021af4927064bb5dfb308e2445fc6b0144820098b8384876e3b28b85b164736f6c634300081a0033"; // 스마트컨트랙트 바이트코드 (생략 가능)

    protected SmartContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(BINARY, contractAddress, web3j, credentials, gasProvider);
    }

    public static SmartContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        return new SmartContract(contractAddress, web3j, credentials, gasProvider);
    }

    // 인바디 정보 등록
    public RemoteFunctionCall<TransactionReceipt> addInbody(
            BigInteger id,
            String createdAt,
            BigInteger weight,
            BigInteger muscleMass,
            BigInteger fatMass,
            BigInteger bmi,
            String armMuscle,
            String trunkMuscle,
            String legMuscle
    ) {
        final Function function = new Function(
                "addInbody",
                Arrays.<Type>asList(
                        new Uint256(id),
                        new Utf8String(createdAt),
                        new Uint256(weight),
                        new Uint256(muscleMass),
                        new Uint256(fatMass),
                        new Uint256(bmi),
                        new Utf8String(armMuscle),
                        new Utf8String(trunkMuscle),
                        new Utf8String(legMuscle)
                ),
                Collections.<TypeReference<?>>emptyList()
        );
        return executeRemoteCallTransaction(function);
    }

    // 인바디 정보 가져오기
    public List<InbodyDto> getMyRecords(Web3j web3j, Credentials credentials, String contractAddress) throws IOException {
        // 스마트 컨트랙트 Web3j 함수 설정
        Function function = new Function(
                "getMyRecords",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<DynamicArray<InbodyStruct>>() {})
        );

        String encoded = FunctionEncoder.encode(function);

        // 저수준 스마트컨트랙트 읽기
        EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, encoded),
                DefaultBlockParameterName.LATEST
        ).send();

        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

        // 반환 리스트
        List<InbodyDto> result = new ArrayList<>();

        if (!decoded.isEmpty()) {
            List<InbodyStruct> records = (List<InbodyStruct>) decoded.get(0).getValue();

            for (DynamicStruct record : records) {
                List<Type> fields = record.getValue();

                InbodyDto dto = new InbodyDto(
                        ((BigInteger) fields.get(0).getValue()).longValue(),
                        fields.get(1).getValue().toString(),
                        ((BigInteger) fields.get(2).getValue()).doubleValue() / 10,
                        ((BigInteger) fields.get(3).getValue()).doubleValue() / 10,
                        ((BigInteger) fields.get(4).getValue()).doubleValue() / 10,
                        ((BigInteger) fields.get(5).getValue()).doubleValue() / 10,
                        fields.get(6).getValue().toString(),
                        fields.get(7).getValue().toString(),
                        fields.get(8).getValue().toString()
                );

                result.add(dto);
            }
        }

        return result;
    }


    public RemoteFunctionCall<TransactionReceipt> myFunction(String arg1) {
        final Function function = new Function(
                "myFunctionName",
                Arrays.<Type>asList(new Utf8String(arg1)),
                Collections.<TypeReference<?>>emptyList());

        return executeRemoteCallTransaction(function);
    }

}
