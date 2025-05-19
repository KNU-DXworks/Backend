package project.DxWorks.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.common.ui.Response;
import project.DxWorks.post.dto.CreatePostRequestDto;
import project.DxWorks.post.dto.PostAllResponseDto;
import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.dto.response.CommunityPostAllRequestDto;
import project.DxWorks.post.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Post 생성
    @PostMapping
    public Response<String> createPost(@RequestPart("data") String json, @RequestPart("image") MultipartFile file, @RequestAttribute("userId") Long userId) throws IOException {
        // form형식 데이터로 받아와서(image때문) 직점 Json 데이터로 수동 변환
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePostRequestDto requestDto = objectMapper.readValue(json, CreatePostRequestDto.class);



        return postService.createPost(requestDto, file, userId);
    }

    // Post 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto requestDto) {
        postService.updatePost(postId, requestDto);
        return ResponseEntity.ok().build();
    }

    // Post 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{community}")
    public Response<CommunityPostAllRequestDto> getCommunityPost(@PathVariable String community) {

        return Response.ok(postService.getCommunityPost(community));
    }
}
