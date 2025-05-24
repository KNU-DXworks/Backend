package project.DxWorks.openai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.DxWorks.openai.dto.ChatCompletionRequest;
import project.DxWorks.openai.dto.ChatCompletionResponse;
import project.DxWorks.openai.dto.Message;

import java.util.Arrays;
import java.util.List;

@Service
public class OpenAiService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.secret-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String classifyBodyType(String userInput) {
        // System 메시지
        String systemPrompt = "you are Body Type Classification Assistant. You classify body types into 8 groups. Only reply with one number from 1 to 8.";

        List<Message> messages = Arrays.asList(new Message("system", systemPrompt), new Message("user", userInput));

        ChatCompletionRequest request = new ChatCompletionRequest(model, messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatCompletionResponse> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, ChatCompletionResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            return response.getBody().getChoices().get(0).getMessage().getContent().trim();
        }
        else {
            throw new RuntimeException("OpenAI 응답 오류: "+response.getStatusCode());
        }
    }


}
