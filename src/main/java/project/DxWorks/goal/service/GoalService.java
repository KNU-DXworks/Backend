package project.DxWorks.goal.service;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.goal.dto.GoalRequestDto;
import project.DxWorks.goal.dto.GoalResponseDto;
import project.DxWorks.goal.entity.BodyType;
import project.DxWorks.goal.entity.BodyTypeLevel;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.goal.repository.GoalRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    /**
     * 내 목표치 생성
     */
    //TODO : figma에는 등록하기 버튼만 있으므로 등록과 수정 한꺼번에 처리했음.
    @Transactional
    public GoalResponseDto createGoal(Long userId, GoalRequestDto requestDto) {
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

        return mapToResponseDto(goal,user.getId());
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

        GoalResponseDto dto = mapToResponseDto(goal, userId);

        return dto;
    }


    private GoalResponseDto mapToResponseDto(Goal goal, Long userId) {
        GoalResponseDto dto = new GoalResponseDto();
        dto.setGoalId(goal.getGoalId());
        dto.setCreatedDate(goal.getCreatedDate());
        dto.setWeight(goal.getWeight());
        dto.setMuscle(goal.getMuscle());
        dto.setFat(goal.getFat());
        dto.setBmi(goal.getBmi());
        dto.setArm(String.valueOf(goal.getArmGrade()));
        dto.setBody(String.valueOf(goal.getBodyGrade()));
        dto.setLeg(String.valueOf(goal.getLegGrade()));
        dto.setGoalBody(String.valueOf(goal.getBodyType()));
        dto.setUserId(userId);
        return dto;
    }


}
