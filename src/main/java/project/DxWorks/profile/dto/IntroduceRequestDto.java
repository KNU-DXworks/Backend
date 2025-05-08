package project.DxWorks.profile.dto;


import lombok.*;


@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class IntroduceRequestDto {
    // private long communityId; //필요한 경우
    private String introduce; // 자기소개 등록/수정용


}
