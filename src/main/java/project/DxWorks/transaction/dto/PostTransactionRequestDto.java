package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostTransactionRequestDto {
    private String traderId;
    private int transactionPeriod;
    private long amount;
    private String info;
}