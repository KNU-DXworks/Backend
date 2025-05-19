package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {
    private long id;
    private String traderId;
    private int transactionPeriod;
    private long amount;
    private String info;
    private String creator;
}