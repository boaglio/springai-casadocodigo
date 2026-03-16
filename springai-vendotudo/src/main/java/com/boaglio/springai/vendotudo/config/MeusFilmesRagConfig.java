package com.boaglio.springai.vendotudo.config;

import com.boaglio.springai.vendotudo.tools.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Configuration
public class MeusFilmesRagConfig {
    
    private static final Logger log = LoggerFactory.getLogger(MeusFilmesRagConfig.class);

    @Value("vectorstore-meus-filmes.json")
    private String vectorStoreName;

    @Value("classpath:/filmes.txt")
    private Resource filmes;

    private final EmbeddingModel embeddingModel;

    public MeusFilmesRagConfig(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Bean
    public SimpleVectorStore getSimpleVectorStoreMeusFilmes() throws IOException {

        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        var vectorStoreFile = FileUtil.getVectorStoreFile(vectorStoreName);
        if (vectorStoreFile.exists()) {

            log.info("Usando arquivo do Vector Store: "+vectorStoreName);
            simpleVectorStore.load(vectorStoreFile);

        } else {

            long startTime = System.currentTimeMillis();
            log.info("Criando Vector Store, carregando...");

            List<Document> documents = Files.readAllLines(Path.of(filmes.getURI()))
                    .stream()
                    .filter(line -> !line.isBlank())
                    .map(line -> new Document(line, Map.of("filename", "filmes.txt")))
                    .toList();

            log.info("Total de filmes carregados: {}", documents.size());

            simpleVectorStore.add(documents);

            log.info("Gravando...");
            simpleVectorStore.save(vectorStoreFile);

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Tempo de execução: {} ms", executionTime);

        }
        return simpleVectorStore;
    }

}