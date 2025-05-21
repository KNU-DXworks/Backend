package project.DxWorks.blockChain.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.blockChain.service.BlockChainService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/block")
public class BlockChainController {

    private final BlockChainService blockchainService;

    @GetMapping
    public String getAllBlocksAndTransactions() {
        try {
            blockchainService.fetchAllBlocksAndTransactions();
            return "Fetched successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
