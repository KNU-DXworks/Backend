package project.DxWorks.GeminiAI.builder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class GeminiTrajectoryPromptBuilder {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String buildPrompt(Map<String, Object> input) {
        try {
            String user = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input.get("user"));
            String others = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input.get("others"));

            return """
                아래는 사용자의 현재 인바디 데이터와 타 사용자들의 과거 인바디 이력 데이터입니다.

                타 사용자들의 과거 데이터를 바탕으로 현재 사용자와 인바디 변화 추이가 유사한 사용자 3명을 추천해주세요.

                유사도 판단 기준은 체중, 근육량, 지방량, BMI, 체형 등급, 근육 등급, 지방 등급, 체형 타입 등입니다.
                단순히 현재 값이 비슷한 것이 아니라, 과거에서 현재로 이어지는 인바디 변화 방향 및 경향성이 유사한 사용자를 3명 선정해야 합니다.

                아래 JSON 형식으로 결과를 반환하세요.

                ### 사용자 현재 인바디 데이터:
                %s

                ### 타 사용자들의 인바디 데이터:
                %s

                ### 결과 형식:
                {
                  "recommand": [
                    {
                      "userId": 1,
                      "reason": "근육량과 지방량, 체형 등급의 변화 추이가 현재 사용자와 거의 일치합니다."
                    }
                  ]
                }
                """.formatted(user, others);

        } catch (Exception e) {
            return "프롬프트 생성 실패: " + e.getMessage();
        }
    }
}
