package project.DxWorks.fileDeal.dto;

import java.math.BigInteger;

public record CreateFileDealRequestDto(
        String privateKey,
        String sellerAddress,
        BigInteger amount
) {}
