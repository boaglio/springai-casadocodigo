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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MeusFilmesRagConfig {
    
    private static final Logger log = LoggerFactory.getLogger(MeusFilmesRagConfig.class);

    @Value("vectorstore-meus-filmes.json")
    private String vectorStoreName;

    @Value("classpath:/filmes.json")
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
                    .map(line -> new Document(line, Map.of("filename", "filmes-anos80.txt")))
                    .toList();

            log.info("Total de filmes carregados: {}", documents.size());

            var counter = new AtomicInteger(0);
            var batchSize = 1000;
            for (int i = 0; i < documents.size(); i += batchSize) {
                List<Document> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
                simpleVectorStore.add(batch);
                var processed = counter.addAndGet(batch.size());
                log.info("Processado: {}/{} ({} %)",
                        processed,
                        documents.size(),
                        (processed * 100) / documents.size());
            }
            log.info("Gravando...");
            simpleVectorStore.save(vectorStoreFile);
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Tempo de execução: {} ms", executionTime);

        }
        return simpleVectorStore;
    }

}