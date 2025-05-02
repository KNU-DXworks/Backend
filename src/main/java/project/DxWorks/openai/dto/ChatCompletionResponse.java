package project.DxWorks.openai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatCompletionResponse {
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        private Message message;
    }
}
