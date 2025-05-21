package project.DxWorks.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionObjectDto {
    private long transactionId;             // 거래 ID
    private long userId;                    // 거래자 ID
    private String name;                    // 거래자 이름
    private String profileImg;              // 거래자 프로필 사진
    private String walletAddress;           // 거래자 지갑 주소
    private int transactionPeriod;          // 거래일수
    private long amount;                    // 거래 금액
    private boolean isTransfered;           // 결제 여부
    private LocalDateTime contractDate;
    private String expirationDate;

}
