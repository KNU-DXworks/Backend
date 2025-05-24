package project.DxWorks.UserRecommend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;
import project.DxWorks.UserRecommend.dto.RecommendUsersDto;
import project.DxWorks.UserRecommend.service.RecommendService;
import project.DxWorks.common.ui.Response;
import project.DxWorks.goal.service.GoalService;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.dto.response.mainpage.RecommendUserDto;
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
    private final GoalService goalService;


    //mypage
    //TODO : 골 dto도 encoding 하여 Bigquery db에 저장된 값과 벡터 유사도 계산 한뒤 top3 추천.로직
    @GetMapping("/recommend")
    public Response<List<RecommendUserDto>> recommendUser(@RequestAttribute Long userId) throws IOException {
        List<RecommendUserDto> result = recommendService.recommendUserByGoal(userId);
        return Response.ok(result);
   }

   @PostMapping("/put/inbodydata/{userId}")
    public Response<EmbeddingRequestDto> putInBodyData(@PathVariable Long userId, @RequestBody InbodyDto data){

       EmbeddingRequestDto dto = recommendService.toEmbeddingRequest(userId,data);
       recommendService.sendToVector(dto);

       return Response.ok(dto);
   }


   @PostMapping("/visualize")
    public Response<String> visualize(@RequestAttribute Long userId){

        try{
            recommendService.visualize3dEmbedding(userId);
            return Response.ok("유저 ID: " + userId + "의 3D 시각화 요청을 성공적으로 보냈습니다.");
        }catch (RuntimeException e){
            return Response.ok("3D 시각화 요청을 실패했습니다." + e.getMessage());
        }
    }

}