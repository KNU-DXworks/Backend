package project.DxWorks.profile.dto;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IntroduceRequestDto {
    private String introduce; // 자기소개 등록/수정용

    private String walletAddress;
}
