package com.boaglio.springai.vendotudo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class TodosFilmesAnos80RagConfig {

    private static final Logger log = LoggerFactory.getLogger(TodosFilmesAnos80RagConfig.class);

    @Value("classpath:/filmes-anos80.txt")
    private Resource filmes;

    private final EmbeddingModel embeddingModel;

    public TodosFilmesAnos80RagConfig(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Bean
    public PgVectorStore getPgVectorStore(JdbcTemplate jdbcTemplate) throws IOException, InterruptedException {

        var pgVectorStore = PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .initializeSchema(true)
                .build();

        pgVectorStore.afterPropertiesSet();

        boolean tableExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                """
                        SELECT EXISTS (
                            SELECT 1 FROM information_schema.tables
                            WHERE table_name = 'vector_store'
                        )
                        """, Boolean.class));

        if (tableExists) {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM vector_store", Integer.class);

            if (count != null && count > 0) {
                log.info("Vector Store já populado com {} registros, pulando carga.", count);
                return pgVectorStore;
            }
        }

        long startTime = System.currentTimeMillis();
        log.info("Criando Vector Store no PostgreSQL...");

        List<Document> documents = Files.readAllLines(Path.of(filmes.getURI()))
                .stream()
                .filter(line -> !line.isBlank())
                .map(line -> new Document(line, Map.of("filename", "filmes-anos80.txt")))
                .toList();

        log.info("Total de filmes carregados: {}", documents.size());

        var counter = new AtomicInteger(0);
        var batchSize = 10;
        for (int i = 0; i < documents.size(); i += batchSize) {
            List<Document> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
            pgVectorStore.add(batch);
            var processed = counter.addAndGet(batch.size());
            log.info("Processado: {}/{} ({} %)",
                    processed,
                    documents.size(),
                    (processed * 100) / documents.size());
        }

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Tempo de execução: {} ms", executionTime);

        return pgVectorStore;
    }
}