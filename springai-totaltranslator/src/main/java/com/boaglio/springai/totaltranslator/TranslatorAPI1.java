package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslatorAPI1 {

    private final ChatClient.Builder chatClient;

    String system = """
        You are an AI powered assistant to translate text only.
        """;

    public TranslatorAPI1(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/api1/portugues-para-ingles/{texto}")
    public String traduzIngles (@PathVariable String texto) {

        var prompt = "Translate from Portuguese to English: %s".formatted(texto);

        return chatClient
                .build()
                .prompt()
                .system(system)
                .user(prompt)
                .call()
                .content();
    }

    @PostMapping("/api1/portugues-para-espanhol/{texto}")
    public String traduzEspanhol (@PathVariable String texto) {

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