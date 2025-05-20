package project.DxWorks.UserRecommend.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.GeminiAI.entity.Inbody;
import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;
import project.DxWorks.UserRecommend.dto.RecommendResponseDto;
import project.DxWorks.UserRecommend.service.RecommendService;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/main")
public class RecommendController {

    private final RecommendService recommendService;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ContractDeployService contractDeployService;

    //mypage
    //TODO : 골 dto도 encoding 하여 Bigquery db에 저장된 값과 벡터 유사도 계산 한뒤 top5 추천.로직
    @GetMapping("/{userId}")
    public Response<RecommendResponseDto> recommend(@PathVariable Long userId) throws IOException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String address = profileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"))
                .getWalletAddress();

        List<InbodyDto> inbodys = contractDeployService.getInbody(address);
        InbodyDto inbody = inbodys.get(inbodys.size()-1);
        EmbeddingRequestDto embeddingDto = recommendService.toEmbeddingRequest(userId,inbody);

        //Flask 서버로  post 전송.
        recommendService.storeEmbedding(embeddingDto);

        if(inbodys.isEmpty()){
            throw new IllegalArgumentException("인바디 데이터가 없습니다.");
        }

        InbodyDto previous = inbodys.get(0);
        InbodyDto current = inbodys.get(inbodys.size()-1);

        RecommendResponseDto dto = new RecommendResponseDto(
                userId,
                user.getUserName(),
                user.getProfile().getProfileUrl(),
                previous.userCase(),
                current.userCase()
        );

        return Response.ok(dto);

    }




}