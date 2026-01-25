package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PiadaTechAPI {

    private final ChatClient.Builder chatClient;

    private final String userPrompt = "Write a short tech joke in Portuguese";

    public PiadaTechAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/nova-piada-tech1")
    public String piadaTech1() {
        return chatClient
                .build()
                .prompt(userPrompt)
                .call()
                .content();
    }

    @GetMapping("/api/nova-piada-tech2")
    public String piadaTech2() {
        return chatClient
                .build()
                .prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    @GetMapping("/api/nova-piada-tech3")
    public ChatResponse piadaTech3() {
        return chatClient
                .build()
                .prompt()
                .user(userPrompt)
                .call()
                .chatResponse();
    }

    @GetMapping("/api/nova-piada-tech4")
    public Flux<String> piadaTech4() {
        return chatClient
                .build()
                .prompt()
                .user(userPrompt+" - repeat 3 times")
                .stream()
                .content();
    }

}