package com.boaglio.springai.totaltranslator;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TotalTranslatorApplication {

	void main(String[] args) {
		SpringApplication.run(TotalTranslatorApplication.class, args);
	}

	@Bean
	public ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(repository)
				.maxMessages(30)
				.build();
	}

}
