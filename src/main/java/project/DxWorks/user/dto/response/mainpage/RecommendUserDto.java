package project.DxWorks.user.dto.response.mainpage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendUserDto {
    private long userId;

    private String userName;

    private String profileImg;

    private String bodyType;

    private String recommendReason;

    private String telegramUrl;
}
