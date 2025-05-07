package project.DxWorks.transaction.struct;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

public class TransactionStruct extends DynamicStruct {

    public Uint256 id;
    public Uint8 transactionType;
    public Address trader;
    public Utf8String transactionCycle;
    public Uint256 transactionPrice;
    public Utf8String transactionDescription;
    public Address creator;

    public TransactionStruct(Uint256 id, Uint8 transactionType, Address trader, Utf8String transactionCycle,
                             Uint256 transactionPrice, Utf8String transactionDescription, Address creator) {
        super(id, transactionType, trader, transactionCycle, transactionPrice, transactionDescription, creator);
        this.id = id;
        this.transactionType = transactionType;
        this.trader = trader;
        this.transactionCycle = transactionCycle;
        this.transactionPrice = transactionPrice;
        this.transactionDescription = transactionDescription;
        this.creator = creator;
    }
}
