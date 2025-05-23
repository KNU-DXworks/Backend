package project.DxWorks.transaction.struct;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

public class TransactionStruct extends DynamicStruct {

    public Uint256 id;
    public Address traderId;
    public Uint256 transactionPeriod;
    public Uint256 amount;
    public Utf8String info;
    public Address creator;

    public TransactionStruct(Uint256 id, Address traderId, Uint256 transactionPeriod,
                             Uint256 amount, Utf8String info, Address creator) {
        super(id, traderId, transactionPeriod, amount, info, creator);
        this.id = id;
        this.traderId = traderId;
        this.transactionPeriod = transactionPeriod;
        this.amount = amount;
        this.info = info;
        this.creator = creator;
    }
}
