package project.DxWorks.goal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.common.ui.Response;
import project.DxWorks.goal.dto.GoalRequestDto;
import project.DxWorks.goal.dto.GoalResponseDto;
import project.DxWorks.goal.entity.BodyType;
import project.DxWorks.goal.entity.BodyTypeLevel;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.goal.service.GoalService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    /**
     * 목표 등록 및 수정
     */

    //TODO : 카카오 로그인 해서 userID 받아오는 것 테스트 확인 해봐야함. desktop에서 client_id, client_secret 발급받아서 해봤지만 안됐음.
    @PutMapping("/goal")
    public Response<GoalResponseDto> createGoal(@RequestAttribute Long userId, @RequestBody GoalRequestDto requestDto) {

        GoalResponseDto created  = goalService.createGoal(userId,requestDto);

        return Response.ok(created);
    }


    /**
     * 나의 목표 조회 (userId로 조회)
     */
    @GetMapping("/goal")
    public Response<GoalResponseDto> getGoalByUserId(@RequestAttribute Long userId) {
        GoalResponseDto created = goalService.findGoalByUserId(userId);
        return Response.ok(created);
    }

}
