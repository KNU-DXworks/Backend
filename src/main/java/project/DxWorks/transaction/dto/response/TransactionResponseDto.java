package project.DxWorks.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.transaction.dto.TransactionDto;

import java.util.List;

@Data
@AllArgsConstructor
public class TransactionResponseDto {

    private List<TransactionDto> sellers;
    private List<TransactionDto> buyers;
}
