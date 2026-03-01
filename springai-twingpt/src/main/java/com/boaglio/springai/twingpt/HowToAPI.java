package com.boaglio.springai.twingpt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HowToAPI {

    private final String userPrompt = "Explain in Portuguese how to ride a bike";

    private final TwinChatService twinChatService;

    public HowToAPI(TwinChatService twinChatService) {
        this.twinChatService = twinChatService;
    }

    @GetMapping("/api/how-to-phi3")
    public String howToPhi3() {
        return twinChatService.phi3answer(userPrompt);
    }

    @GetMapping("/api/how-to-ministral3")
    public String howToMinistral3() {
        return twinChatService.ministral3Answer(userPrompt);
    }


    @GetMapping("/api/eval/how-to")
    public String evalAnswer(String pergunta) {
        String answer;
        try {
            answer = twinChatService.evalAnswer(pergunta);
        } catch ( GuardrailViolationException gve) {
            answer = gve.getMessage();
        }
        return answer;
    }

}