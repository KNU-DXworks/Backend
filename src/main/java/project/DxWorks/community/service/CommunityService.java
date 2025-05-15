package project.DxWorks.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DxWorks.community.entity.Community;
import project.DxWorks.community.repository.CommunityRepository;

import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.dto.PostResponseDto;
import project.DxWorks.post.entity.Post;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 커뮤니티별 게시글 목록 조회
//    public List<PostResponseDto> getPostsByCommunity(Long communityId) {
//        return postRepository.findAllByCommunity(communityId).stream()
//                .map(PostResponseDto::from)
//                .toList();
//    }

    // 커뮤니티에 게시글 생성
//    @Transactional
//    public Long createPost(Long communityId, PostRequestDto dto) {
//
//        UserEntity user = userRepository.findById(dto.userId())
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//
//        Community community = communityRepository.findById(communityId)
//                .orElseThrow(() -> new NoSuchElementException("Community not found"));
//
//        Post post = new Post(user, community, dto.content(), dto.postImg());
//
//        community.addPost(post);
//
//        communityRepository.save(community);
//
//        return post.getId();
//    }
}

