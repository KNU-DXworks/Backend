package project.DxWorks.goal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.goal.dto.GoalRequestDto;
import project.DxWorks.goal.dto.GoalResponseDto;
import project.DxWorks.goal.entity.BodyTypeLevel;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.goal.service.GoalService;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    /**
     * 목표 등록
     */
    @PostMapping
    public ResponseEntity<Long> createGoal(@RequestBody GoalRequestDto requestDto) {
        Goal goal = convertToGoal(requestDto);
        Long goalId = goalService.createGoal(goal, requestDto.getUserId());
        return ResponseEntity.ok(goalId);
    }


    /**
     * 목표 조회 (userId로 조회)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<GoalResponseDto> getGoalByUserId(@PathVariable Long userId) {
        Goal goal = goalService.findGoalByUserId(userId);
        return ResponseEntity.ok(convertToDto(goal));
    }

    /**
     * 목표 조회 (goalId로 조회)
     */
    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponseDto> getGoalById(@PathVariable Long goalId) {
        Goal goal = goalService.findGoalById(goalId);
        return ResponseEntity.ok(convertToDto(goal));
    }


    /**
     * 목표 수정
     */
    @PutMapping("/user/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable Long goalId, @RequestBody GoalRequestDto requestDto) {
        Goal goal = convertToGoal(requestDto);
        goalService.updateGoal(goal, goalId);
        return ResponseEntity.ok().build();
    }

    /**
     * 목표 삭제
     */
    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }




    /**
     * GoalRequestDto를 Goal 엔티티로 변환
     */
    private Goal convertToGoal(GoalRequestDto requestDto) {
        Goal goal = new Goal();

        goal.setWeight(requestDto.getWeight());
        goal.setMuscle(requestDto.getMuscle());
        goal.setFat(requestDto.getFat());
        goal.setBmi(requestDto.getBmi());

        goal.setArm(BodyTypeLevel.fromString(requestDto.getArm()));
        goal.setBody(BodyTypeLevel.fromString(requestDto.getBody()));
        goal.setLeg(BodyTypeLevel.fromString(requestDto.getLeg()));

        goal.setGoalGroup(requestDto.getGoalGroup());
        return goal;
    }

    /**
     * Goal 엔티티를 GoalResponseDto로 변환
     */
    private GoalResponseDto convertToDto(Goal goal) {
        GoalResponseDto responseDto = new GoalResponseDto();

        responseDto.setCreatedDate(goal.getCreatedDate());
        responseDto.setWeight(goal.getWeight());
        responseDto.setMuscle(goal.getMuscle());
        responseDto.setFat(goal.getFat());
        responseDto.setBmi(goal.getBmi());

        responseDto.setArm(String.valueOf(goal.getArm()));
        responseDto.setBody(String.valueOf(goal.getBody()));
        responseDto.setLeg(String.valueOf(goal.getLeg()));

        responseDto.setGoalGroup(goal.getGoalGroup());
        return responseDto;
    }

}
