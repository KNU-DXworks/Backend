package project.DxWorks.post.dto.response.myProfile;


import lombok.AllArgsConstructor;
import lombok.Data;
import project.DxWorks.community.entity.CommunityCategory;
import project.DxWorks.goal.entity.BodyType;

@Data
@AllArgsConstructor
public class GoalDto {
    private Double weight;

    private Double muscle;

    private Double fat;

    private BodyType bodyType;
}
