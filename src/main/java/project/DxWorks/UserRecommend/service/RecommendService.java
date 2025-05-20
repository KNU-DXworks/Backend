package project.DxWorks.UserRecommend.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;
import project.DxWorks.UserRecommend.dto.RecommendResponseDto;
import project.DxWorks.common.ui.Response;
import project.DxWorks.inbody.dto.InbodyDto;
import project.DxWorks.inbody.service.ContractDeployService;
import project.DxWorks.inbody.struct.InbodyStruct;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final ContractDeployService contractDeployService;



    public void storeEmbedding(EmbeddingRequestDto dto){
        String url = "http://localhost:5000/api/gemini/embeddinginbody"; // Flask 서버 주소

        try {
            RestTemplate restTemplate = new RestTemplate();
            //JSON 변환
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(Map.of(
                    "userId", dto.userId(),
                    "vector", dto.vector()
            ));


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url,request, String.class);

            if(response.getStatusCode().is4xxClientError()){
                throw new RuntimeException("API returned client error " + response.getBody());
            }
            if(response.getBody().contains("<html>")){
                throw new RuntimeException("HTML 에러가 API로부터 발생.");
            }
            if(!response.getStatusCode().is2xxSuccessful()){
                throw new RuntimeException("Gemini API are failed: " + response.getStatusCode() + " - " + response.getBody());
            }

            if(response.getStatusCode().is2xxSuccessful()){
                System.out.println("파이썬 서버에 벡터 저장 성공.");
            }else{
                System.out.println("실패 : " + response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //InbodyStruce 의 내용들을 벡터db에 임베딩 시키기 위해 double 형태로 인코딩.
    public EmbeddingRequestDto toEmbeddingRequest(long userId,InbodyDto inbodyDto) {
        double bodyTypeEncoded;
        double armGradeEncoded;
        double bodyGradeEncoded;
        double legGradeEncoded;
        double genderEncoded;

        String bodyType = inbodyDto.userCase();
        String armGrade = inbodyDto.armGrade();
        String bodyGrade = inbodyDto.bodyGrade();
        String legGrade = inbodyDto.legGrade();
        String gender = inbodyDto.gender();
        String createdAtStr = inbodyDto.createdAt();


        //String CreatedAt -> double 형식으로 변환.
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
        double createdTime = createdAt.toEpochSecond(ZoneOffset.UTC);


        bodyTypeEncoded = switch (bodyType) {
                case "SKINNY" -> 0.0;
                case "SKINNY_MUSCLE" -> 1.0;
                case "STANDARD" -> 2.0;
                case "WEIGHT_LOSS" -> 3.0;
                case "MUSCLE" -> 4.0;
                case "OVERWEIGHT" -> 5.0;
                case "OBESITY" -> 6.0;
                case "MUSCULAR_OBESITY" -> 7.0;
                case "WEIGHT" -> 8.0;
                default -> -1.0;
            };
        armGradeEncoded = switch (armGrade) {
                case "BELOW_STANDARD" -> 0.0;
                case "STANDARD" -> 1.0;
                case "ABOVE_STANDARD" -> 2.0;
                default -> -1.0;
            };
        bodyGradeEncoded = switch (bodyGrade) {
            case "BELOW_STANDARD" -> 0.0;
            case "STANDARD" -> 1.0;
            case "ABOVE_STANDARD" -> 2.0;
            default -> -1.0;
        };
        legGradeEncoded = switch (legGrade) {
            case "BELOW_STANDARD" -> 0.0;
            case "STANDARD" -> 1.0;
            case "ABOVE_STANDARD" -> 2.0;
            default -> -1.0;
        };
        genderEncoded = switch (gender) {
            case "FEMALE" -> 0.0;
            case "MALE" -> 1.0;
            default -> -1.0;
        };

        List<Double> vector = List.of(
                createdTime,
                genderEncoded,
                inbodyDto.height(),
                inbodyDto.weight(),
                inbodyDto.muscle(),
                inbodyDto.fat(),
                inbodyDto.bmi(),
                bodyTypeEncoded,
                armGradeEncoded,
                bodyGradeEncoded,
                legGradeEncoded
        );
        return new EmbeddingRequestDto(userId,vector);
    }


}
