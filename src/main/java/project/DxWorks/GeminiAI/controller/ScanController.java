package project.DxWorks.GeminiAI.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.GeminiAI.entity.Inbody;
import project.DxWorks.GeminiAI.service.GeminiService;
import project.DxWorks.GeminiAI.service.InbodyService;
import project.DxWorks.common.domain.exception.ErrorCode;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.PostInbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class ScanController {

    private final InbodyService inbodyService;

    private final ContractDeployService contractDeployService;


    @Operation(
            summary = "인바디 이미지 업로드",
            description = "인바디 사진을 업로드하여 분석된 데이터를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 및 분석 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping
    public Response<Inbody> uploadInbodyImage(@RequestParam("file") MultipartFile file, @RequestHeader("X-PRIVATE-KEY") String privateKey) {
        try {

            Inbody saved = inbodyService.analyzeAndSave(file);

            PostInbodyDto dto = new PostInbodyDto(
                    saved.getId(),
                    saved.getCreatedAt(),
                    saved.isInbodySheet(),
                    saved.getGender(),
                    saved.getWeight(),
                    saved.getHeight(),
                    saved.getMuscle(),
                    saved.getFat(),
                    saved.getBmi(),
                    saved.getBodyType(),
                    saved.getArmGrade(),
                    saved.getBodyGrade(),
                    saved.getLegGrade(),
                    privateKey
            );
            contractDeployService.addInbody(dto);
            return Response.ok(saved);

        } catch (IOException e) {
            return Response.error(ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e + "kkkk");
        }
    }

}
