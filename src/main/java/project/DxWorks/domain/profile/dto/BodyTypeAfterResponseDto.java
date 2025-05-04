package project.DxWorks.domain.profile.dto;

import lombok.Data;


public class BodyTypeAfterResponseDto {
    private String bodyTypeAfter;

    // TODO : inbody 도메인에서 첫 번째가 아닌 인바디 등록을 할 경우 변화된 체형 조회
//    public BodyTypeAfterResponseDto(Inbody inbody) {
//        this.bodyTypeAfter = inbody.getBodyTypeAfter();
//    }
}
