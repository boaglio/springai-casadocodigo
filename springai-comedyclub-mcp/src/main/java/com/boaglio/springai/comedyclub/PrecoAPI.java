package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrecoAPI {

    private final ChatClient.Builder chatClient;
    private final ToolCallbackProvider mcpToolProvider;

    public PrecoAPI(ChatClient.Builder chatClient, ToolCallbackProvider mcpToolProvider) {
        this.chatClient = chatClient;
        this.mcpToolProvider = mcpToolProvider;
    }

    @GetMapping("/api/preco/{pergunta}")
    public String qualPreco(@PathVariable String pergunta) {

        var userPrompt = "Always answer in Portuguese. Each movie costs US$5, but a famous movie is US$10. Requested Movie: ";

        return chatClient
                .build()
                .prompt()
                .toolCallbacks(mcpToolProvider)
                .user(userPrompt+pergunta)
                .call()
                .content();
    }


    @GetMapping("/api/famoso/{filme}")
    public String famoso(@PathVariable String filme) {

        var userPrompt = "Return true or false if this requested movie is famous: ";

        return chatClient
                .build()
                .prompt()
                .toolCallbacks(mcpToolProvider)
                .user(userPrompt+filme)
                .call()
                .content();
    }

}