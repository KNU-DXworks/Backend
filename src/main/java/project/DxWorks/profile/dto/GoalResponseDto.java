package project.DxWorks.profile.dto;

public class GoalResponseDto {

    private Long goalId;
    private Double goalWeight; // 목표 체중 (회원가입시 작성)
    private Double goalMuscle; // 목표 골격근량
    private Double goalFat; // 목표 체지방
    private String bodyType;

    // TODO : (회원가입시) Goal 도메인에서 조회를 위한 DTO
//    public GoalResponseDto(Goal goal){
//        this.goalWeight = goal.getGoalWeight();
//        this.goalMuscle = goal.getGoalMuscle();
//        this.goalFat = goal.getGoalFat();
//        this.bodyType = goal.getBodyType();
//    }

}
