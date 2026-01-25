package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelpBotAPI {

    private final ChatClient.Builder chatClient;

    public HelpBotAPI(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    String system = """
        Besides the user message also inform that we have
        APIs that can translate text from Portuguese to English
        or Spanish.
        """;

    @GetMapping("/api1/ajuda")
    public String ajuda1 (@RequestParam String pergunte) {
        return chatClient
                .build()
                .prompt()
                .user(pergunte)
                .call()
                .content();
    }

    @GetMapping("/api2/ajuda")
    public String ajuda2 (@RequestParam String pergunte) {
        return chatClient
                .build()
                .prompt()
                .system(system)
                .user(pergunte)
                .call()
                .content();
    }

}