package project.DxWorks.userRecommend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.userRecommend.service.RecommendService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;


    // TODO : requestAttribute로 수정
    @GetMapping("/{userId}")
    public ResponseEntity<List<Map<String, Object>>> recommend(@PathVariable Long userId) {
        List<Map<String, Object>> recommendations = recommendService.recommendUsers(userId);
        return ResponseEntity.ok(recommendations);
    }

//    토큰 사용하는 경우
//    @GetMapping
//    public ResponseEntity<List<Map<String, Object>>> recommend(Principal principal) {
//        Long userId = Long.parseLong(principal.getName()); // 혹은 CustomUserDetails.getId()
//        List<Map<String, Object>> recommendations = recommendService.recommendUsers(userId);
//        return ResponseEntity.ok(recommendations);
//    }
}
