package project.DxWorks.inbody.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.dto.PostInbodyRequestDto;
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

    @GetMapping("/test/{ctAdd}")
    public String get(@PathVariable String ctAdd) {
        try {
            return contractDeployService.callMessage(ctAdd);
        } catch (Exception e) {
            return "Read Block failed: " + e.getMessage();
        }
    }

    @PostMapping("/api/inbody")
    public String addInbody(@RequestBody PostInbodyRequestDto requestDto) throws Exception {
        return contractDeployService.addInbody(requestDto);
    }

    @GetMapping("/api/inbody/{privateKey}")
    public List<InbodyDto> getInbody(@PathVariable String privateKey) throws IOException {
        return contractDeployService.getInbody(privateKey);
    }
}
