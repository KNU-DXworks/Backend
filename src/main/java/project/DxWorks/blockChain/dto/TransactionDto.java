package project.DxWorks.blockChain.dto;

public record TransactionDto(
        String txHash,
        String from,
        String to,
        String eth
)
{ }
