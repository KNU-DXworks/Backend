package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {
    private long id;            // 거래 ID
    private String seller;      // 판매자 주소
    private String buyer;       // 구매자 주소
    private int transactionPeriod;
    private long amount;
    private String info;
    private boolean paid;       // 결제 여부
}