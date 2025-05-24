package project.DxWorks.GeminiAI.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.GeminiAI.dto.RecommendationDto;
import project.DxWorks.GeminiAI.dto.RecommendationResponseDto;
import project.DxWorks.GeminiAI.entity.Inbody;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
    public class GeminiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    @Value("${gemini.api-key}")
    private String geminiApiKey;

    public GeminiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    //인바디 데이터 추출하고 JSON 방식으로 변환하는 메서드
    //TODO : 인바디 공식 사진만 추출하게,
    public Inbody extractInbodyData(MultipartFile file) throws IOException {
            //예시 : Gemini API를 통해 OCR 및 분석 결과를 추출한다고 가정
            //실제 구현 시 HTTP POST로 이미지 보내고 JSON 받아서 파싱 필요
            // TODO : Gemini API 연동 및 파싱


            //0. API 키 발급 -> 2.0 flash 사용 , 버젼마다 json 변환하는 모양 다름 주의!
            // TODO : API 키 값 application.properties에 따로 저장!
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            //1. 파일 ->Base64 인코딩
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            //2. Gemini API 요청 Body 생성.
            Map<String,Object> inlineData = new HashMap<>();
            inlineData.put("mimeType", "image/png");
            inlineData.put("data", base64Image);

            // image 와 text part 나눔.

            //TODO : 프롬포팅 새로하기 계속 다른 체형 받아옴.
            //image part
            Map<String, Object> imagePart = new HashMap<>();
            imagePart.put("inlineData", inlineData);
            //test part
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", "The official InBody result sheet usually contains the InBody logo on the top-left, and contains specific sections in Korean like '체성분분석', '골격근-지방분석', and tabular data.\n" +
                    "If the image is **not** an official InBody result sheet\n " +
                    "return false else true boolean value \n" +
                    "Extract the following values from an Inbody result sheet image: \n" +
                "gender(if male return MALE,if female,return FEMALE)\n" +
                "weight (kg)\n" +
                    "height (cm)\n" +
                "muscle (kg)\n" +
                "fat(kg)\n" +
                "BMI\n" +
                "For the following muscle parts, return their standard status as one of the following values:" +
                 "if 표준이하 return BELOW_STANDARD, if 표준 return STANDARD, if 표준이상 or HIGH return ABOVE_STANDARD" +
                "muscleMass\n" +
                "fatMass\n" +
                "arm muscle\n" +
                "body muscle\n" +
                "leg muscle\n\n" +
                "Then calculate the following:\n" +
                "muscle mass ratio = (skeletal muscle mass / weight) * 100 (round to 1 decimal place)\n\n" +
                "body fat percentage = (body fat mass / weight) * 100 (round to 1 decimal place)\n\n" +
                // 체형 분류 요청
                "Finally, based on gender, muscle mass ratio, and body fat percentage, classify the body type into one of the 8 categories: " +
                "SKINNY, SKINNY_MUSCLE, STANDARD, WEIGHT_LOSS, MUSCLE, OVERWEIGHT, OBESITY, MUSCULAR_OBESITY\n" +
                "For males:\n" +
                    "\n" +
                    "body fat % ≤ 10 → low\n" +
                    "\n" +
                    "10 < body fat % ≤ 20 → normal\n" +
                    "\n" +
                    "body fat % > 20 → high\n" +
                    "\n" +
                    "muscle mass ratio ≤ 32 → low\n" +
                    "\n" +
                    "32 < muscle mass ratio ≤ 38 → normal\n" +
                    "\n" +
                    "muscle mass ratio > 38 → high" +
                "\n" +
                "2. For females:\n" +
                    "\n" +
                    "body fat % ≤ 18 → low\n" +
                    "\n" +
                    "18 < body fat % ≤ 28 → normal\n" +
                    "\n" +
                    "body fat % > 28 → high\n" +
                    "\n" +
                    "muscle mass ratio thresholds same as males" +
                "\n" +
                "[Type Mapping]\n" +
                "\n" +
                "- fat: low, muscle: low → SKINNY\n" +
                "- fat: low, muscle: high → SKINNY_MUSCLE\n" +
                "- fat: normal, muscle: normal → STANDARD\n" +
                "- fat: high, muscle: low → WEIGHT_LOSS\n" +
                "- fat: normal, muscle: high → MUSCLE\n" +
                "- fat: high, muscle: normal → OVERWEIGHT\n" +
                "- fat: high, muscle: low → OBESITY\n" +
                "- fat: high, muscle: high → MUSCULAR_OBESITY\n" +
                "Return the final result in JSON format. Use the following keys " +
                "inbodySheet,gender, weight, height, muscle, fat ,bmi, armGrade, bodyGrade, legGrade, " +
                "muscleMassType, fatMassType,bodyType");

        List<Map<String, Object>> parts = List.of(imagePart, textPart);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", parts);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String,Object>> request = new HttpEntity<>(requestBody,headers);

            //3. 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);

            //4. 결과 파싱
            String result = response.getBody();


            //5. 전체 응답을 먼저 파싱해서 text 추출.
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(result);
            JsonNode text = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            if(text.isMissingNode()){
                throw new RuntimeException("Gemini 응답에서 Text를 찾을 수 없습니다.");
            }

            String rawText = text.asText();

            // 6. 텍스트에서 '''json 블럭 제거
            String jsonText = extractJsonFromResponse(rawText);

            // 7. JSON 문자열을 Inbody 로 변환.
            return mapper.readValue(jsonText, Inbody.class);
    }
    // Gemini 응답은 전체 text를 포함하므로 json 부분만 추출하는 메서드
    private String extractJsonFromResponse(String content) throws IOException {

        // 백틱 감싸진 부분 제거
        int start = content.indexOf("'''json");
        if (start == -1) {
            start = content.indexOf("'''");
        }

        int end = content.lastIndexOf("'''");

        if(start != -1 && end != -1 && start < end){
            content = content.substring(start+6,end).trim();  // "```json" 이후부터 마지막 백틱 전까지
        }

        // 혹시 모르니 {로 시작하고 }로 끝나도록 다시 잘라줌
        int jsonStart = content.indexOf("{");
        int jsonEnd = content.lastIndexOf("}");
        if(jsonStart != -1 && jsonEnd != -1 && jsonStart < jsonEnd){
            return content.substring(jsonStart,jsonEnd+1);
        }
        throw new RuntimeException("Json 파싱 실패 : 응답에서 JSON을 찾을수 없음");
    }

    public List<RecommendationDto> recommendSimilarUsersByTrajectory(Map<String, Object> inputData) {
        try {
            // 사용자 및 타 사용자 인바디 정보 JSON 변환
            ObjectMapper mapper = new ObjectMapper();
            String user = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("user"));
            String others = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputData.get("others"));

            // 프롬프트 생성
            String prompt = """
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

            // 요청 바디 구성
            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(textPart));
            Map<String, Object> requestBody = Map.of("contents", List.of(content));

            // HTTP 요청 구성
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // URL 구성
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            // 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String result = response.getBody();

            // 응답 파싱
            JsonNode root = objectMapper.readTree(result);
            String innerJson = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            System.out.println("innerData:" + innerJson);

            String cleanedJson = innerJson
                    .replaceAll("^```json\\s*", "")
                    .replaceAll("\\s*```$", "")
                    .trim();


            RecommendationResponseDto respon =
                    objectMapper.readValue(cleanedJson, RecommendationResponseDto.class);



//            if (textNode.isMissingNode()) {
//                throw new RuntimeException("Gemini 응답에서 Text를 찾을 수 없습니다.");
//            }

            return respon.recommand();

        } catch (Exception e) {
            throw new RuntimeException("Gemini 유사 사용자 추천 실패: " + e.getMessage(), e);
        }
    }
}