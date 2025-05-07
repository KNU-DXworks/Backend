package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {
    private long id;
    private int transactionType;
    private String trader;
    private String transactionCycle;
    private long transactionPrice;
    private String transactionDescription;
    private String creator;
}