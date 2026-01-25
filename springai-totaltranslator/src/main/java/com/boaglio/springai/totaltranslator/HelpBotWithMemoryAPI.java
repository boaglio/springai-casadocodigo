package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelpBotWithMemoryAPI {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public HelpBotWithMemoryAPI(ChatClient.Builder chatClient, ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
        this.chatClient = chatClient
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(system)
                .build() ;
    }

    String system = """
        Besides the user message also inform that we have
        APIs that can translate text from Portuguese to English
        or Spanish.
        """;

    @GetMapping("/api/ajuda/{usuario}")
    String pergunta(@PathVariable String usuario, @RequestParam String pergunta) {
        return this.chatClient
                .prompt()
                .user(pergunta)
                .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, usuario))
                .call()
                .content();
    }

}