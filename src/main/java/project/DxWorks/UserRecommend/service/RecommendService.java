    package project.DxWorks.UserRecommend.service;


    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.*;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestTemplate;
    import project.DxWorks.UserRecommend.dto.EmbeddingRequestDto;
    import project.DxWorks.UserRecommend.dto.RecommendUsersDto;
    import project.DxWorks.UserRecommend.dto.SimilarUserDto;
    import project.DxWorks.goal.dto.GoalResponseDto;
    import project.DxWorks.goal.service.GoalService;
    import project.DxWorks.inbody.dto.InbodyDto;
    import project.DxWorks.inbody.service.ContractDeployService;
    import project.DxWorks.profile.entity.Profile;
    import project.DxWorks.profile.repository.ProfileRepository;
    import project.DxWorks.user.domain.UserEntity;
    import project.DxWorks.user.dto.response.mainpage.RecommendUserDto;
    import project.DxWorks.user.repository.UserRepository;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.LocalDateTime;
    import java.time.ZoneOffset;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class RecommendService {

        private final ContractDeployService contractDeployService;
        private final GoalService goalService;
        private final UserRepository userRepository;
        private final ProfileRepository profileRepository;
        GoalResponseDto goalResponseDto;
        RecommendUserDto recommandUserDto;

        @Transactional
        //Flask 서버로 gemini가 스캔한 인바디의 인코딩된 정보 넘겨 json 파일 형태로 저장.
        public void storeEmbedding(EmbeddingRequestDto dto) {
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
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

                if (response.getStatusCode().is4xxClientError()) {
                    throw new RuntimeException("API returned client error " + response.getBody());
                }
                if (response.getBody().contains("<html>")) {
                    throw new RuntimeException("HTML 에러가 API로부터 발생.");
                }
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Gemini API are failed: " + response.getStatusCode() + " - " + response.getBody());
                }

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("파이썬 서버에 벡터 저장 성공.");
                } else {
                    System.out.println("실패 : " + response.getStatusCode());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Transactional
        //Flask 서버로 인코딩된 내 목표치 정보 넘겨서 Flask에서 벡터디비의 다른 인바디들과 유사도 계산.
        public List<SimilarUserDto> sendToGoal(List<Double> encodedGoal) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:5000/api/main/recommend";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("goal_vector", encodedGoal);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            System.out.println("추천 결과 : " + response.getBody());

            //JSON 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            List<SimilarUserDto> recommendUsers = new ArrayList<>();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode top3 = root.get("top3");

                if (top3 != null && top3.isArray()) {
                    for (JsonNode userNode : top3) {
                        Long userId = userNode.get("userId").asLong();
                        Double similarity = userNode.get("similarity").asDouble();
                        recommendUsers.add(new SimilarUserDto(userId, similarity));
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return recommendUsers;
        }

        @Transactional
        public List<RecommendUserDto> recommendUserByGoal(Long userId) {
            //1. 사용자 목표치 조회 및 인코딩
            GoalResponseDto goalDto = goalService.findGoalByUserId(userId);
            if (goalDto == null) {
                throw new IllegalArgumentException("나의 목표치가 없습니다. " + userId);
            }
            List<Double> encoded = EncodingGoal(goalDto);
            System.out.println("목표 벡터 : " + encoded); //디버깅용

            //2. Flask 서버로 유사 사용자 조회
            List<SimilarUserDto> similarUserDtos = sendToGoal(encoded);

            //3. 각 사용자에 대해 인바디 및 정보 조회
            return similarUserDtos.stream()
                    .distinct()
                    .limit(3)
                    .map(dto -> {
                        Long recommendUserId = dto.userId();
                        UserEntity recommendUser = userRepository.findById(recommendUserId)
                                .orElseThrow(() -> new IllegalArgumentException("추천 유저 정보가 없습니다."));
                        Profile profile = profileRepository.findByUser(recommendUser)
                                .orElseThrow(() -> new IllegalArgumentException("추천 유저의 프로필이 없습니다"));
                        String userName = recommendUser.getUserName();
                        String profileImg = profile.getProfileUrl();
                        String walletAddress = profile.getWalletAddress();

                        List<InbodyDto> inbodys;
                        try {
                            inbodys = contractDeployService.getInbody(walletAddress);
                        } catch (IOException e) {
                            throw new RuntimeException("추천 유저 인바디 데이터를 가져오는 중 오류 발생");
                        }
                        if (inbodys.isEmpty()) {
                            throw new IllegalArgumentException("추천 유저의 인바디 데이터가 없습니다.");
                        }
                        String prevType = inbodys.get(0).userCase();
                        String bodyType = inbodys.get(inbodys.size() - 1).userCase();

                        return new RecommendUserDto(recommendUserId, userName, profileImg, prevType, bodyType);
                    }).collect(Collectors.toList());

        }


        // flask 시각화 서버 포트번호 5001 사용.
        @Transactional
        public void visualize3dEmbedding(Long userId){
            String url =  "http://localhost:5001/api/main/visualize";
            //목표치 조회
            GoalResponseDto goalDto = goalService.findGoalByUserId(userId);
            if(goalDto == null){
                throw new IllegalArgumentException("목표치가 없습니다.");
            }
            List<Double> goalEncoded = EncodingGoal(goalDto);
            System.out.println("목표 벡터 : " + goalEncoded);

            try{
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(Map.of("goal_vector",goalEncoded));

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request = new HttpEntity<>(json, headers);
                ResponseEntity<byte[]> response = restTemplate.postForEntity(url, request, byte[].class);

                if(response.getStatusCode().is2xxSuccessful()){
                    byte[] images = response.getBody();
                    Path outputPath = Paths.get("Inbody_3D_visualization.png");
                    Files.write(outputPath, images);
                    System.out.println("3D 시각화 저장 완료 : " +outputPath.toAbsolutePath());
                }else{
                    throw new RuntimeException("시각화 요청 실패 : " + response.getBody());
                }
            }catch(IOException e){
                throw new RuntimeException("이미지 저장 실패 : " + e.getMessage());
            }
        }


        @Transactional
        //Inbodydto 내용들을 벡터db에 임베딩 시키기 위해 double 형태로 인코딩.
        public EmbeddingRequestDto toEmbeddingRequest(Long userId, InbodyDto inbodyDto) {
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

            System.out.println("1");
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
            return new EmbeddingRequestDto(userId, vector);
        }

        @Transactional
        //목표치 인코딩
        public List<Double> EncodingGoal(GoalResponseDto dto) {
            double bodyTypeEncoded = switch (dto.getBodyType()) {
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

            double armGradeEncoded = switch (dto.getArmGrade()) {
                case "BELOW_STANDARD" -> 0.0;
                case "STANDARD" -> 1.0;
                case "ABOVE_STANDARD" -> 2.0;
                default -> -1.0;
            };

            double bodyGradeEncoded = switch (dto.getBodyGrade()) {
                case "BELOW_STANDARD" -> 0.0;
                case "STANDARD" -> 1.0;
                case "ABOVE_STANDARD" -> 2.0;
                default -> -1.0;
            };

            double legGradeEncoded = switch (dto.getLegGrade()) {
                case "BELOW_STANDARD" -> 0.0;
                case "STANDARD" -> 1.0;
                case "ABOVE_STANDARD" -> 2.0;
                default -> -1.0;
            };

            return List.of(
                    -1.0, //createTime 존재x
                    -1.0, // gender 존재 x
                    -1.0,// height 존재x
                    nullToZero(dto.getWeight()),
                    nullToZero(dto.getMuscle()),
                    nullToZero(dto.getFat()),
                    nullToZero(dto.getBmi()),
                    bodyTypeEncoded,
                    armGradeEncoded,
                    bodyGradeEncoded,
                    legGradeEncoded
            );
        }

        private double nullToZero(Double value) {
            return value == null ? 0 : value;

        }

        @Transactional
        //Flask 서버로  넘겨서  벡터디비에 내가 입력한 인바디 데이터 삽입
        public void sendToVector(EmbeddingRequestDto dto) {
            String url = "http://localhost:5000/api/gemini/embeddinginbody";

            try {
                RestTemplate restTemplate2 = new RestTemplate();
                ObjectMapper objectMapper = new ObjectMapper();

                String json = objectMapper.writeValueAsString(Map.of(
                        "userId", dto.userId(),
                        "vector", dto.vector()
                ));


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request = new HttpEntity<>(json, headers);
                ResponseEntity<String> response = restTemplate2.postForEntity(url, request, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Flask 서버로의 전송 실패 : " + response.getBody());
                }
                System.out.println("벡터 DB에 인바디 정보 저장 성공 " + response.getBody());
            }catch (JsonProcessingException e){
                throw new RuntimeException("JSON 직렬화 실패 " + e);
            }
        }
    }
