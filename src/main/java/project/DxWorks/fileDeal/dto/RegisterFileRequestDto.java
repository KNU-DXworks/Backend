package project.DxWorks.fileDeal.dto;

import java.math.BigInteger;

public record RegisterFileRequestDto(
        String privateKey,
        BigInteger dealId,
        String ipfsHash
) {}
