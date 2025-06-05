package project.DxWorks.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckTransactionRequestDto {
    private String userName;             // 구매자 텔레그램 userName
}
