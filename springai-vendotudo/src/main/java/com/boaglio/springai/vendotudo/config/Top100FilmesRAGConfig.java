package com.boaglio.springai.vendotudo.config;

import com.boaglio.springai.vendotudo.tools.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Top100FilmesRAGConfig   {

    private static final Logger log = LoggerFactory.getLogger(Top100FilmesRAGConfig.class);

    private final EmbeddingModel embeddingModel;

    @Value("vectorstore-top100filmes-anos80.json")
    private String vectorStoreName;

    @Value("classpath:/imdb-100GreatestMoviesOfThe1980s.pdf")
    private Resource pdfTodosFilmesAnos80;

    public Top100FilmesRAGConfig(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Bean
    public SimpleVectorStore getSimpleVectorStoreTop100Filmes() throws IOException {

        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        var vectorStoreFile = FileUtil.getVectorStoreFile(vectorStoreName);
        if (vectorStoreFile.exists()) {

            log.info("Usando arquivo do Vector Store: "+vectorStoreName);
            simpleVectorStore.load(vectorStoreFile);

        } else {

            long startTime = System.currentTimeMillis();
            log.info("Criando Vector Store, carregando...");

            var pdfReader = new PagePdfDocumentReader(pdfTodosFilmesAnos80,
                    PdfDocumentReaderConfig.builder()
                            .withPagesPerDocument(1) // one Document per page
                            .build());

//            List<Document> documents = pdfReader.get().stream()
//                    .flatMap(doc -> Arrays.stream(doc.getText().split("\n")))
//                    .map(String::trim)
//                    .filter(line -> !line.isBlank())
//                    .map(line -> new Document(line, Map.of("source", "filmes-anos80.pdf")))
//                    .toList();
//
//            log.info("Total de filmes extraídos: {}", documents.size());

            var textSplitter = new TokenTextSplitter(
                    10,  // chunkSize
                    5,  // minChunkSizeChars
                    3,    // minChunkLengthToEmbed
                    10000, // maxNumChunks
                    true  // keepSeparator
            );

            List<Document> rawDocuments = pdfReader.get();
            log.info("Páginas/Parágrafos extraídos: {}", rawDocuments.size());

            List<Document> documents = textSplitter.apply(pdfReader.get())
                    .stream()
                    .peek(doc -> doc.getMetadata().put("source", "filmes-anos80.pdf"))
                    .toList();
            log.info("Chunks após tokenização: {}", documents.size());

            log.info("VectorStore Loaded with data!");
            simpleVectorStore.add(documents);

            log.info("Gravando...");
            simpleVectorStore.save(vectorStoreFile);
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Tempo de execução: {} ms", executionTime);

        }
        return simpleVectorStore;
    }

}