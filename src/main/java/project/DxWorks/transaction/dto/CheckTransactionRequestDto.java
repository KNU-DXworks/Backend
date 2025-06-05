package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckTransactionRequestDto {
    private String userName;             // 구매자 텔레그램 userName
}
