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

    public Inbody extractInbodyData(MultipartFile file) throws IOException {
            //예시 : Gemini API를 통해 OCR 및 분석 결과를 추출한다고 가정
            //실제 구현 시 HTTP POST로 이미지 보내고 JSON 받아서 파싱 필요
            // TODO : Gemini API 연동 및 파싱

ge
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

            //image part
            Map<String, Object> imagePart = new HashMap<>();
            imagePart.put("inlineData", inlineData);
            //test part
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", "인바디 결과지에서 다음 값들을 추출해줘: \n" +
                "성별(남자면 male 여자면 female)\n" +
                "몸무게 (kg)\n" +
                "골격근량 (kg)\n" +
                "체지방량 (kg)\n" +
                "BMI\n" +
                "**다음 항목들의 '표준여부'는 모두 한국어로 추출해줘:**\\n" +
                "골격근량의 표준여부\n" +
                "체지방량의 표준여부\n" +
                "BMI의 표준여부\n" +
                "팔근육의 표준여부\n" +
                "몸통근육의 표준여부\n" +
                "다리근육의 표준여부\n\n" +
                "그리고 다음 수치를 계산해:\n" +
                "근육량 비율 = (골격근량 / 체중) * 100 (소수점 첫 번째 자리까지만)\n\n" +
                "체지방률 = (체지방량 / 체중) * 100 (소수점 첫 번째 자리까지만)\n\n" +
                // 체형 분류 요청
                "남성과 여성을 각각 다음 기준에 따라 남성과 여성 각각 다음 기준에 따라 근육량 비율과 체지방률을 기준으로 체형 유형을 판단해줘.\n" +
                "각각의 체형은 다음 8가지 중 하나: 마른체형, 마른근육형, 표준형, 감량형, 근육형, 과체중형, 비만형, 비만근육형\n" +
                "1. 남성:\n" +
                "- 체지방률 ≤ 10% → low\n" +
                "- 체지방률 10~20% → normal\n" +
                "- 체지방률 ≥ 20% → high\n" +
                "- 근육량 비율 ≤ 32% → low\n" +
                "- 근육량 비율 32~38% → normal\n" +
                "- 근육량 비율 ≥ 38% → high\n" +
                "\n" +
                "2. 여성:\n" +
                "- 체지방률 ≤ 18% → low\n" +
                "- 체지방률 18~28% → normal\n" +
                "- 체지방률 ≥ 28% → high\n" +
                "- 근육량 비율 기준은 동일 (남녀 모두)\n" +
                "\n" +
                "[체형 유형 분류]\n" +
                "\n" +
                "- fat: low, muscle: low → 마른체형\n" +
                "- fat: low, muscle: high → 마른근육형\n" +
                "- fat: normal, muscle: normal → 표준형\n" +
                "- fat: high, muscle: low → 감량형\n" +
                "- fat: normal, muscle: high → 근육형\n" +
                "- fat: high, muscle: normal → 과체중형\n" +
                "- fat: high, muscle: low → 비만형\n" +
                "- fat: high, muscle: high → 비만근육형\n" +
                "최종결과는 JSON으로 반환해주고 value 중 표준여부 관련 값들은 모두 한국어로 반환해주고 key 값은 영어로 다음과 같이 해줘\n " +
                "gender, weight, muscleMass, fatMass,muscleMassRatio,fatRatio bmi, armMuscleType, trunkMuscleType, legMuscleType." +
                "muscleMassType, fatMassType,bmiType, userCase");

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
}
