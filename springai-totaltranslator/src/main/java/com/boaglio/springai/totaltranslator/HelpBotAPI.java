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
        You are an customer service assistant for Total Translator.
        You can ONLY discuss:
         - translate texts from English to Portuguese
         - translate texts from Spanish to Portuguese
        Answer in Portuguese only.
        If asked about anything else, respond:
        "Desculpe, a ajuda é apenas sobre traduções".
        """;

    @GetMapping("/api1/ajuda")
    public String ajuda1 (@RequestParam String pergunta) {
        return chatClient
                .build()
                .prompt()
                .user(pergunta)
                .call()
                .content();
    }

    @GetMapping("/api2/ajuda")
    public String ajuda2 (@RequestParam String pergunta) {
        return chatClient
                .build()
                .prompt()
                .system(system)
                .user(pergunta)
                .call()
                .content();
    }

}