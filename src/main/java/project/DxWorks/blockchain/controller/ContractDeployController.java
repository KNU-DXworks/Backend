package project.DxWorks.blockchain.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.blockchain.service.ContractDeployService;

@RestController
@AllArgsConstructor
public class ContractDeployController {

    private final ContractDeployService contractDeployService;

    @PostMapping("/test")
    public String deploy() {
        try {
            return contractDeployService.deployContract();
        } catch (Exception e) {
            return "Deploy failed: " + e.getMessage();
        }
    }
}
