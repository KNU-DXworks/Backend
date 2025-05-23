package project.DxWorks.transaction.struct;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

public class TransactionStruct extends DynamicStruct {

    public Uint256 id;
    public Address seller;
    public Address buyer;
    public Uint256 transactionPeriod;
    public Uint256 amount;
    public Utf8String info;
    public Bool paid;
    public Uint256 createdAt;

    public TransactionStruct(Uint256 id, Address seller, Address buyer, Uint256 transactionPeriod, Uint256 amount, Utf8String info, Bool paid, Uint256 createdAt) {
        super(id, seller, buyer, transactionPeriod, amount, info, paid, createdAt);
        this.id = id;
        this.seller = seller;
        this.buyer = buyer;
        this.transactionPeriod = transactionPeriod;
        this.amount = amount;
        this.info = info;
        this.paid = paid;
        this.createdAt = createdAt;
    }
}
