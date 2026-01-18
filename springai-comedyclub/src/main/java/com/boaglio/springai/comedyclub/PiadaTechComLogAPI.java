package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PiadaTechComLogAPI {

    private final ChatClient.Builder chatClient;

    SimpleLoggerAdvisor customLogger = new SimpleLoggerAdvisor(
            request -> "Request: " + request.prompt().getUserMessage(),
            response -> "Response: " + response.getResults(),
            0
    );

    public PiadaTechComLogAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/nova-piada-tech-com-log")
    public String piadaTechComLog() {
        var userPrompt = "Write a tech joke in Portuguese";
        return chatClient
                .build()
                .prompt()
                .advisors(customLogger)
                .user(userPrompt)
                .call()
                .content();
    }

}