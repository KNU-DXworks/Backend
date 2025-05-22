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
import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;
import project.DxWorks.UserRecommend.dto.RecommendResponseDto;
import project.DxWorks.UserRecommend.service.BigQueryService;
import project.DxWorks.UserRecommend.service.RecommendService;
import project.DxWorks.common.domain.exception.ErrorCode;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.dto.PostInbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class ScanController {

    private final InbodyService inbodyService;

    private final ContractDeployService contractDeployService;

    private final RecommendService recommendService;

    private final UserRepository userRepository;


    @Operation(
            summary = "인바디 이미지 업로드",
            description = "인바디 사진을 업로드하여 분석된 데이터를 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 및 분석 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping("/inbody")
    public Response<Inbody> uploadInbodyImage(@RequestParam("file") MultipartFile file, @RequestHeader("X-PRIVATE-KEY") String privateKey,
                                              @RequestAttribute Long userId) {
        try {
            //TODO : 로그인된 사용자의 id를 bigquery에 저장하기 위함.
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 Id가 없습니다." + userId));
            Inbody saved = inbodyService.analyzeAndSave(file);


            //임베딩을 위한 dto
            InbodyDto embeddingDto = new InbodyDto(
                    saved.getCreatedAt().toString(),
                    saved.getGender(),
                    saved.getWeight(),
                    saved.getHeight(),
                    saved.getMuscle(),
                    saved.getFat(),
                    saved.getBmi(),
                    saved.getBodyType(),
                    saved.getArmGrade(),
                    saved.getBodyGrade(),
                    saved.getLegGrade()
            );

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
            System.out.println("1");
            //System.out.println(saved.getId()); // 0으로 받아옴.
            System.out.println(user.getId());

//          //String 형태 -> double로 인코딩 한 후 dto 전달.
            EmbeddingRequestDto embeddingRequestDto = recommendService.toEmbeddingRequest(user.getId(),embeddingDto);
            //Flask 서버로 POST
            //TODO : userId가 BigQuery에 들어가지 않음. saved.getId()가 0으로 고정됨.
            recommendService.storeEmbedding(embeddingRequestDto);
            return Response.ok(saved);


        } catch (IOException e) {
            return Response.error(ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

}
