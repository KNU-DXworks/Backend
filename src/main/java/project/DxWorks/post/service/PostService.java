package project.DxWorks.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.entity.Post;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.user.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

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


        if (post.getCommunity() != null) {
            post.getCommunity().removePost(post);
        }

        postRepository.delete(post);
    }
}
