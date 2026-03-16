package com.boaglio.springai.vendotudo.api;

import org.springframework.ai.chat.client.ChatClient;
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
                        .build() )
                .call()
                .content();
    }

    @GetMapping("/api/rag/todos-filmes-anos80-debug")
    public List<String> debug(@RequestParam String pergunta) {

        return vectorStore.similaritySearch(
                      SearchRequest.builder()
                              .query(pergunta)
                              .topK(TOP_K)
                              .similarityThreshold(SIMILARITY_THRESHOLD)
                              .build())
              .stream()
              .map(Document::getText)
              .toList();
    }

}