package project.DxWorks.goal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * 목표 생성
     */
    @Transactional
    public Long createGoal(Goal goal, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found, userId: "+userId));

        user.createGoal(goal);

        goal.setCreatedDate(LocalDateTime.now());
        goalRepository.save(goal);

        return goal.getGoalId();
    }

    /**
     * 목표 조회 (userId로 조회)
     */
    public Goal findGoalByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Goal not found, userId: "+userId))
                .getGoal();
    }

    /**
     * 목표 조회 (goalId로 조회)
     */
    public Goal findGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new NoSuchElementException("Goal not found, goalId: "+goalId));
    }

    /**
     * 목표 수정
     */
    @Transactional
    public void updateGoal(Goal goalData, Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NoSuchElementException("Goal not found, userId: "+goalId));

        if (goalData.getWeight() != null)
            goal.setWeight(goalData.getWeight());
        if (goalData.getMuscle() != null)
            goal.setMuscle(goalData.getMuscle());
        if (goalData.getFat() != null)
            goal.setFat(goalData.getFat());
        if (goalData.getBmi() != null)
            goal.setBmi(goalData.getBmi());

        if (goalData.getArm() != null)
            goal.setArm(goalData.getArm());
        if (goalData.getBody() != null)
            goal.setBody(goalData.getBody());
        if (goalData.getLeg() != null)
            goal.setLeg(goalData.getLeg());

        if (goalData.getGoalBody() != null)
            goal.setGoalBody(goalData.getGoalBody());

    }

    /**
     * 목표 삭제
     */
    @Transactional
    public void deleteGoal(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NoSuchElementException("Goal not found, goalId: "+goalId));

        goalRepository.delete(goal);
    }


}
