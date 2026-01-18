package com.boaglio.springai.twingpt;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiOllamaConfig {

    public static final double DETERMINISTICO = 0.3;
    public static final double CRIATIVO = 0.9;

    @Bean
    public OllamaChatModel ministral3ChatModel(OllamaApi ollamaApi) {
        return OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(OllamaChatOptions.builder()
                .model("ministral-3:3b")
                .temperature(CRIATIVO)
                .build())
            .build();
    }

    @Bean
    public OllamaChatModel phi3ChatModel(OllamaApi ollamaApi) {
        return OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(OllamaChatOptions.builder()
                .model("phi3")
                .temperature(DETERMINISTICO)
                .build())
            .build();
    }
}
