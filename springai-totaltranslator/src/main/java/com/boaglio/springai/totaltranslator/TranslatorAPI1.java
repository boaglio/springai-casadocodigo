package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class TranslatorAPI1 {

    private final ChatClient.Builder chatClient;

    String system = """
        You are an AI powered assistant to translate text only.
        """;

    public TranslatorAPI1(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api1/portugues-para-ingles")
    public String traduzIngles (@RequestParam String texto) {

        var prompt = "Translate from Portuguese to English: %s".formatted(texto);

        return chatClient
                .build()
                .prompt()
                .system(system)
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/api1/portugues-para-espanhol")
    public String traduzEspanhol (@RequestParam String texto) {

        var prompt = "Translate from Portuguese to Spanish: %s".formatted(texto);

        return chatClient
                .build()
                .prompt()
                .system(system)
                .user(prompt)
                .call()
                .content();
    }

}