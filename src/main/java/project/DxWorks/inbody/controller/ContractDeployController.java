package project.DxWorks.inbody.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.dto.PostInbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;

import java.io.IOException;
import java.util.List;

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


    @PostMapping("/api/inbody")
    public String addInbody(@RequestBody PostInbodyDto requestDto) throws Exception {
        return contractDeployService.addInbody(requestDto);
    }

    @GetMapping("/api/inbody/{walletAddress}")
    public List<InbodyDto> getInbody(@PathVariable String walletAddress) throws IOException {
        return contractDeployService.getInbody(walletAddress);
    }
}
