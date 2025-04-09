package project.DxWorks.blockchain.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.blockchain.dto.PostInbodyRequestDto;
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

    @GetMapping("/test/{ctAdd}")
    public String get(@PathVariable String ctAdd) {
        try {
            return contractDeployService.callMessage(ctAdd);
        } catch (Exception e) {
            return "Read Block failed: " + e.getMessage();
        }
    }

    @PostMapping("/test/inbody")
    public String addInbody(@RequestBody PostInbodyRequestDto requestDto) throws Exception {
        return contractDeployService.addInbody(requestDto);
    }
}
