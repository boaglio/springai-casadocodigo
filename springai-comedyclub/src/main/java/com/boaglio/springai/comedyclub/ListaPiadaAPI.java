package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListaPiadaAPI {

    private final ChatClient.Builder chatClient;

    public ListaPiadaAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/lista-piadas")
    public List<Piadas> listaPiadas() {

        var systemPrompt = "I am an AI system that generates response without double quotes and only valid text for a JSON field";
        var userPrompt = "Write three jokes in Portuguese";

        return chatClient
                .build()
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

}