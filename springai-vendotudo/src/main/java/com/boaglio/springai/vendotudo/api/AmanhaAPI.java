package com.boaglio.springai.vendotudo.api;

import com.boaglio.springai.vendotudo.tools.DateTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AmanhaAPI {

    private final ChatClient.Builder chatClient;

    public AmanhaAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/amanha")
    public String amanha () {
        return chatClient
                .build()
                .prompt("What day is tomorrow?")
                .call()
                .content();
    }

    @GetMapping("/api/amanha2")
    public String amanha2 () {
        return chatClient
                .build()
                .prompt("What day is tomorrow?")
                .tools(new DateTools())
                .call()
                .content();

    }
}
