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

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
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
    @PostMapping("/api/upload")
    public ResponseEntity<Inbody> uploadInbodyImage(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("파일 이름: " + file.getOriginalFilename());
            Inbody saved = inbodyService.analyzeAndSave(file);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(
            summary = "업로드된 인바디 전체 조회",
            description = "저장된 모든 인바디 분석 데이터를 조회합니다."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/api/getupload")
    public ResponseEntity<List<Inbody>> getAll() {
        return ResponseEntity.ok(inbodyService.getAll());
    }
}
