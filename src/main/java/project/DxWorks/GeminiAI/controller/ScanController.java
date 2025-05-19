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

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class ScanController {

    private final InbodyService inbodyService;


    @Operation(
            summary = "인바디 이미지 업로드",
            description = "인바디 사진을 업로드하여 분석된 데이터를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 및 분석 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/inbody")
    public Response<Inbody> uploadInbodyImage(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("파일 이름: " + file.getOriginalFilename());
            Inbody saved = inbodyService.analyzeAndSave(file);
            return Response.ok(saved);

        } catch (IOException e) {
            return Response.error(ErrorCode.INTERNAL_ERROR);
        }
    }
    @Operation(
            summary = "업로드된 인바디 전체 조회",
            description = "저장된 모든 인바디 분석 데이터를 조회합니다."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/getinbody")
    public Response<List<Inbody>> getAll() {
        return Response.ok(inbodyService.getAll());
    }
}
