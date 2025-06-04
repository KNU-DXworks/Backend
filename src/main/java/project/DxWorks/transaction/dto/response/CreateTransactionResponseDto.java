package project.DxWorks.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateTransactionResponseDto {

    private String userName;
    private String walletAddress;
    private String profileUrl;
    private CommunityCategory community;
}
