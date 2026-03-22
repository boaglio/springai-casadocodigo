package com.boaglio.springai.vendotudo.api;

import com.boaglio.springai.vendotudo.tools.DateTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarioAPI {

    private final ChatClient chatClient;

    String system = "Answer **always in Portuguese**";
    String userInput = "What is the current year ?";

    public CalendarioAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem(system)
                .build() ;
    }

    @GetMapping("/api/ano")
    public String esseAno () {
        return chatClient
                .prompt(userInput)
                .call()
                .content();
    }

    @GetMapping("/api/ano-com-tooling")
    public String esseAno2 () {
        return chatClient
                .prompt(userInput)
                .tools(new DateTools())
                .call()
                .content();

    }
}
