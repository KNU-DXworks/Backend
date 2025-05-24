package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostTransactionRequestDto {
    private String buyerId;             // 구매자 주소
    private int transactionPeriod;      // 거래 기간
    private long amount;                // 금액
    private String info;                // 설명
}