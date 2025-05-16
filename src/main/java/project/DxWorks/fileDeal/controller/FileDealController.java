package project.DxWorks.fileDeal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.fileDeal.dto.CreateFileDealRequestDto;
import project.DxWorks.fileDeal.dto.RegisterFileRequestDto;
import project.DxWorks.fileDeal.service.FileDealService;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/filedeal")
public class FileDealController {

    private final FileDealService fileDealService;

    @PostMapping("/test")
    public String deploy() {
        try {
            return fileDealService.deployContract();
        } catch (Exception e) {
            return "Deploy failed: " + e.getMessage();
        }
    }

    @PostMapping("/create")
    public String createDeal(@RequestBody CreateFileDealRequestDto dto) throws Exception {
        return fileDealService.createDeal(dto);
    }

    @PostMapping("/register")
    public String registerFile(@RequestBody RegisterFileRequestDto dto) throws Exception {
        return fileDealService.registerFile(dto);
    }

    @PostMapping("/confirm/{dealId}")
    public String confirmDelivery(@PathVariable BigInteger dealId) throws Exception {
        return fileDealService.confirmDelivery(dealId);
    }


}
