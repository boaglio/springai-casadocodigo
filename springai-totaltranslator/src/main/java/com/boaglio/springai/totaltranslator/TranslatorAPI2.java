package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TranslatorAPI2 {

    String system = """
        You are an AI powered assistant to translate text only.
        Translate everything from Portuguese to {idioma}.
        """;

    private final ChatClient.Builder chatClient;

    public TranslatorAPI2(ChatClient.Builder chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/api2/portugues-para-ingles/{texto}")
    public String traduzIngles (@PathVariable String texto) {

        return chatClient
                .build()
                .prompt()
                .system(sp -> sp.text(system).params(Map.of("idioma","English")))
                .user(texto)
                .call()
                .content();
    }

    @PostMapping("/api2/portugues-para-espanhol/{texto}")
    public String traduzEspanhol (@PathVariable String texto) {

        return chatClient
                .build()
                .prompt()
                .system(sp -> sp.text(system).params(Map.of("idioma","Spanish")))
                .user(texto)
                .call()
                .content();
    }

}