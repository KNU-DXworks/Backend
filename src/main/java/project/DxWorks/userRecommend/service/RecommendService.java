package project.DxWorks.userRecommend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.DxWorks.goal.entity.Goal;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;
import project.DxWorks.userRecommend.dto.RecommendRequestDto;
import project.DxWorks.userRecommend.dto.TransformationRecordDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final BigQueryService bigQueryService;


    // TODO : startGroup -> currentBodyType 이런식으로 이름 바꿀것.
    public List<Map<String, Object>> recommendUsers(Long userId) {

        System.out.println("사용자ID 찾기 전");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Goal goal = user.getGoal();
        System.out.println("사용자ID = " + user.getId());

        String startGroup = user.getCurrentBody().name(); // 현재 체형 그룹
        String goalGroup = goal.getGoalBody().name();     // 목표 체형 그룹

        RecommendRequestDto dto = new RecommendRequestDto(
                startGroup,
                goalGroup,
                goal.getWeight(),
                goal.getMuscle(),
                goal.getFat()
        );
        System.out.println("RecommendRequestDto 작성 전");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RecommendRequestDto> request = new HttpEntity<>(dto, headers);

        System.out.println("RecommendRequestDto 작성 후");
        System.out.println("request = " + request);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:8000/recommend", request, Map.class
        );

        return (List<Map<String, Object>>) response.getBody().get("recommended_users");
    }

    // TODO : 목표를 달성한 유저의 이전 그룹, 현재 그룹, 체중, 근육량, 지방량
    // 유저의 그룹이 바뀌면 마다 이전 그룹, 현재 그룹, 체중, 근육량, 지방량을 벡터디비로 insert ==> 추후 사용 예정
    public void checkAndSaveIfGoalReached(UserEntity user) {
        Goal goal = user.getGoal();


        TransformationRecordDto record = new TransformationRecordDto(
                user.getId(),
                user.getUserName(),
                user.getCurrentBody().name(),
                goal.getGoalBody().name(),
                goal.getWeight(),
                goal.getMuscle(),
                goal.getFat(),
                LocalDate.now()
        );
        bigQueryService.saveUserTransformation(record);

    }
}
