package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ChatConfig {

    private static final int DEFAULT_MAX_MESSAGES = 20;

    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        var jdbc = JdbcChatMemoryRepository
                .builder()
                .dataSource(dataSource)
                .build();
        var mwa = MessageWindowChatMemory
                .builder()
                .maxMessages(DEFAULT_MAX_MESSAGES)
                .chatMemoryRepository(jdbc)
                .build();
        return PromptChatMemoryAdvisor
                .builder(mwa)
                .build();
    }

}
