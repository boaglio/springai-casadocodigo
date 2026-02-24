package com.boaglio.springai.vendotudo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RAGTodosFilmesAnos80API {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final int TOP_K = 1;
    private final double SIMILARITY_THRESHOLD=0.4;

    Logger log = LoggerFactory.getLogger(RAGTodosFilmesAnos80API.class.getName());

    SimpleLoggerAdvisor customLogger = SimpleLoggerAdvisor.builder()
            .requestToString(request -> {
                String userMsg1 = request.prompt().getUserMessage().getText();
                String userMsg2 = request.prompt().getContents();
                var contexts1 = request.context().keySet().stream().toList();
                var contexts2 = request.context().values().stream().toList();
                String systemMsg = request.prompt().getSystemMessage().getText();
                return  "\n- SYSTEM- \n" + systemMsg +
                        "\n- RAG1  - \n" + contexts1 +
                        "\n- RAG2  - \n" + contexts2 +
                        "\n- USER1 - \n" + userMsg1 +
                        "\n- USER2 - \n" + userMsg2;
            })
            .responseToString(response -> "\n-  RESPONSE - \n" + response.getResults())
            .build();

    String system = """
            You are a movie database assistant.
            Answer in Portuguese only.

            STRICT RULES:
            - You can ONLY answer based on the movies listed in the CONTEXT below
            - If the movie is NOT in the CONTEXT, say: "Esse filme não está na nossa base de dados."
            - Do NOT use your general knowledge about movies
            - Do NOT invent or assume information not present in the CONTEXT
            - If asked about a movie, confirm if it exists in the CONTEXT and provide its year if available
        """;

    public RAGTodosFilmesAnos80API(ChatClient.Builder chatClient,
                                   @Qualifier("getPgVectorStore") VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient
                .defaultAdvisors(customLogger)
                .defaultSystem(system)
                .build() ;
    }

    @GetMapping("/api/rag/todos-filmes-anos80")
    public String vendedor (@RequestParam String pergunta) {
        return chatClient
                .prompt()
                .user(pergunta)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder()
                                .topK(TOP_K)
                                .similarityThreshold(SIMILARITY_THRESHOLD)
                                .build())
                        .build(),
                        customLogger) 
                .call()
                .content();
    }

    @GetMapping("/api/rag/todos-filmes-anos80/debug")
    public List<String> debug(@RequestParam String pergunta) {

          List<String> semanticResults = vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(pergunta)
                                .topK(TOP_K)
                                .similarityThreshold(SIMILARITY_THRESHOLD)
                                .build())
                .stream()
                .map(Document::getText)
                .toList();

        log.info("Semantic results: {}", semanticResults);

        return semanticResults;
    }

}