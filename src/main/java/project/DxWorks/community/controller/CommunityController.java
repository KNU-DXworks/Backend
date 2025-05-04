package project.DxWorks.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.DxWorks.community.service.CommunityService;
import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.dto.PostResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/{communityId}")
    public ResponseEntity<List<PostResponseDto>> getPosts(@PathVariable Long communityId) {
        return ResponseEntity.ok(communityService.getPostsByCommunity(communityId));
    }

    @PostMapping("/{communityId}")
    public ResponseEntity<Long> createPost(
            @PathVariable Long communityId,
            @RequestBody PostRequestDto dto) {
        return ResponseEntity.ok(communityService.createPost(communityId, dto));
    }
}
