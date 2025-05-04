package project.DxWorks.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IntroduceRequestDto {
    private long userId;
    private String introduce; // 자기소개 등록/수정용

}
