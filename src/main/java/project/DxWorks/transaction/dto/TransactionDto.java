package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    private long id;            // 거래 ID
    private String seller;      // 판매자 주소
    private String buyer;       // 구매자 주소
    private int transactionPeriod;
    private BigDecimal amount;
    private String info;
    private boolean paid;       // 결제 여부
    private LocalDateTime createdAt;
}