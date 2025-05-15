package project.DxWorks.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.common.ui.Response;
import project.DxWorks.fileSystem.service.FileService;
import project.DxWorks.post.dto.CreatePostRequestDto;
import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.entity.Post;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final FileService fileService;

    // Post 생성
    @Transactional
    public Response<String> createPost(CreatePostRequestDto requestDto, MultipartFile file) throws IOException {
        UserEntity userEntity = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()-> new NoSuchElementException("해당하는 user가 존재하지 않습니다.: " + requestDto.getUserId()));

        String url = fileService.uploadFile(file);

        Post post = new Post(
                userEntity,
                requestDto.getCommunityType(),
                requestDto.getContent(),
                url,
                requestDto.getPostType()
        );

        postRepository.save(post);

        return Response.ok("게시글 생성 성공");
    }

    // Post 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        post.setContent(dto.content());
        post.setPostImg(dto.postImg());
    }

    // Post 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        postRepository.delete(post);
    }
}
