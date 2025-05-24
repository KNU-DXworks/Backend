package project.DxWorks.goal.service;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.UserRecommend.service.RecommendService;
import project.DxWorks.common.ui.Response;
import project.DxWorks.goal.dto.GoalRequestDto;
import project.DxWorks.goal.dto.GoalResponseDto;
import project.DxWorks.goal.entity.BodyType;
import project.DxWorks.goal.entity.BodyTypeLevel;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.goal.repository.GoalRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final RecommendService recommendService;


    /**
     * 내 목표치 생성
     */
    //TODO : figma에는 등록하기 버튼만 있으므로 등록과 수정 한꺼번에 처리했음.
    @Transactional
    public String createGoal(Long userId, GoalRequestDto requestDto) throws IOException {
        //사용자 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다.: "+userId));

        Goal goal = user.getGoal();
        if(goal == null) {
            //등록 로직
            goal = new Goal();
            goal.setWeight(requestDto.getWeight());
            goal.setMuscle(requestDto.getMuscle());
            goal.setFat(requestDto.getFat());
            goal.setBmi(requestDto.getBmi());
            goal.setArmGrade(BodyTypeLevel.valueOf(requestDto.getArmGrade()));
            goal.setBodyGrade(BodyTypeLevel.valueOf(requestDto.getBodyGrade()));
            goal.setLegGrade(BodyTypeLevel.valueOf(requestDto.getLegGrade()));
            goal.setBodyType(BodyType.valueOf(requestDto.getBodyType()));

            goalRepository.save(goal);
            user.createGoal(goal);
        }else {
            //수정 로직
            if (requestDto.getWeight() != null)
                goal.setWeight(requestDto.getWeight());
            if (requestDto.getMuscle() != null)
                goal.setMuscle(requestDto.getMuscle());
            if (requestDto.getFat() != null)
                goal.setFat(requestDto.getFat());
            if (requestDto.getBmi() != null)
                goal.setBmi(requestDto.getBmi());

            if (requestDto.getArmGrade() != null)
                goal.setArmGrade(BodyTypeLevel.valueOf(requestDto.getArmGrade()));
            if (requestDto.getBodyGrade() != null)
                goal.setBodyGrade(BodyTypeLevel.valueOf(requestDto.getBodyGrade()));
            if (requestDto.getLegGrade() != null)
                goal.setLegGrade(BodyTypeLevel.valueOf(requestDto.getLegGrade()));

            if (requestDto.getBodyType() != null)
                goal.setBodyType(BodyType.valueOf(requestDto.getBodyType()));
        }

        GoalResponseDto goalDto = findGoalByUserId(userId);
        if(goalDto == null){
            throw new IllegalArgumentException("나의 목표치가 없습니다. " + userId);
        }
        List<Double> encoded = recommendService.EncodingGoal(goalDto);
        System.out.println("목표 벡터 : " + encoded); //디버깅용

        recommendService.getRecommandTop10(userId, encoded);

        return "추천 사용자가 추가됐습니다!";
        }

    /**
     * 나의 목표치 조회 (userId로 조회) :
     */
    public GoalResponseDto findGoalByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 목표치가 없습니다." + userId));

        Goal goal = user.getGoal();
        if(goal == null){
            throw new NoSuchElementException("사용자 id에 해당하는 목표치가 없습니다." + userId);
        }

        GoalResponseDto dto = mapToResponseDto(goal);

        return dto;
    }


    private GoalResponseDto mapToResponseDto(Goal goal) {
        GoalResponseDto dto = new GoalResponseDto();
        dto.setWeight(goal.getWeight());
        dto.setMuscle(goal.getMuscle());
        dto.setFat(goal.getFat());
        dto.setBmi(goal.getBmi());
        dto.setArmGrade(String.valueOf(goal.getArmGrade()));
        dto.setBodyGrade(String.valueOf(goal.getBodyGrade()));
        dto.setLegGrade(String.valueOf(goal.getLegGrade()));
        dto.setBodyType(String.valueOf(goal.getBodyType()));
        return dto;
    }


}
