package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateTransactionRequestDto {
    private String userName;             // 구매자 텔레그램 userName
    private int transactionPeriod;      // 거래 기간
    private BigDecimal amount;                // 금액
    private String info;                // 설명
}
