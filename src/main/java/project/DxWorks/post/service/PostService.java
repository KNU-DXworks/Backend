package project.DxWorks.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.common.ui.Response;
import project.DxWorks.fileSystem.service.FileService;
import project.DxWorks.post.domain.CommunityType;
import project.DxWorks.post.dto.CreatePostRequestDto;
import project.DxWorks.post.dto.PostAllResponseDto;
import project.DxWorks.post.dto.PostRequestDto;
import project.DxWorks.post.dto.response.CommunityPostAllRequestDto;
import project.DxWorks.post.entity.Post;
import project.DxWorks.post.repository.PostRepository;
import project.DxWorks.profile.entity.Profile;
import project.DxWorks.profile.repository.ProfileRepository;
import project.DxWorks.user.domain.UserEntity;
import project.DxWorks.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final FileService fileService;

    private final ProfileRepository profileRepository;

    // Post 생성
    @Transactional
    public Response<String> createPost(CreatePostRequestDto requestDto, MultipartFile file, Long userId) throws IOException {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new NoSuchElementException("해당하는 user가 존재하지 않습니다.: " + userId));

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

    //커뮤니티 모든 게시물 조회
    @Transactional
    public CommunityPostAllRequestDto getCommunityPost(String community) {
        List<PostAllResponseDto> list =  postRepository.findAllByCommunityType(CommunityType.valueOf(community))
                .stream()
                .map(post -> {
                    UserEntity user = post.getUser();
                    Profile profile = profileRepository.findByUser(user)
                            .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자입니다."));

                    return new PostAllResponseDto(
                        post.getId(),
                        user.getId(),
                        user.getUserName(),
                        profile.getProfileUrl(),
                        post.getRegDt(),
                        profile.getCommunity(),
                        post.getContent(),
                        post.getPostImg(),
                        resolveFileType(post.getPostImg())
                    );
                })
                .toList();

        return new CommunityPostAllRequestDto(CommunityType.valueOf(community), list);
    }

    public String resolveFileType(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return "NONE";
        if (fileUrl.endsWith(".pdf")) return "application/pdf";
        if (fileUrl.matches(".*\\.(jpg|jpeg|png|gif|bmp)$")) return "image/jpeg";
        return "UNKNOWN";
    }
}
