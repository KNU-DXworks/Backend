package project.DxWorks.openai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DxWorks.openai.service.OpenAiService;

@RestController
@RequestMapping("/body-type")
public class BodyTypeController {

    private final OpenAiService openAiService;

    public BodyTypeController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping
    public ResponseEntity<String> classify(@RequestBody String userInput) {
        String result = openAiService.classifyBodyType(userInput);
//        System.out.println("OpenAI에서 받은 응답으로 " + result + "번 군집입니다!");

        return ResponseEntity.ok(result);
    }

}
