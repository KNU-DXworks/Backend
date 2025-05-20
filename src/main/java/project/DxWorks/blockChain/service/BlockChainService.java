package project.DxWorks.blockChain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;

@Service
@AllArgsConstructor
public class BlockChainService {

    private final Web3j web3j;

    public void fetchAllBlocksAndTransactions() throws IOException {
        // 최신 블록 번호 가져오기
        long latestBlock = web3j.ethBlockNumber().send().getBlockNumber().longValue();

        for (long i = 0; i <= latestBlock; i++) {
            EthBlock ethBlock = web3j.ethGetBlockByNumber(
                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(BigInteger.valueOf(i)),
                    true // 트랜잭션 포함 여부
            ).send();

            EthBlock.Block block = ethBlock.getBlock();
            System.out.println("Block #" + block.getNumber());

            block.getTransactions().forEach(tx -> {
                EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();
                System.out.println("  TxHash: " + transaction.getHash());
                System.out.println("  From: " + transaction.getFrom());
                System.out.println("  To: " + transaction.getTo());
                System.out.println("  Value: " + transaction.getValue());
            });
        }
    }
}
