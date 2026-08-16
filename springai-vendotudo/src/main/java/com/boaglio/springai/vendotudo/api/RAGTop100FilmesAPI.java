package com.boaglio.springai.vendotudo.api;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RAGTop100FilmesAPI {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final int TOP_K = 5;
    private final double SIMILARITY_THRESHOLD=0.7;

    String system = """
        You are a movie assistant of movies of the eighties.
        You will be provided with context containing information about these movies retrieved from a vector store.  
        **Use only that context** to answer questions about the movies. 
        - If the user asks about a movie **not present in the context**, respond:  
          "Sorry, that movie is not available in my list."  
        - If the movie is in the context, provide the relevant information (cast, director, plot, etc.) based on the context.   
        - Answer **always in Portuguese**.  
        - Do not invent any movie details not present in the context.
        """;

    public RAGTop100FilmesAPI(ChatClient.Builder chatClient,
                              @Qualifier("getSimpleVectorStoreTop100Filmes") VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .topK(TOP_K)
                                        .similarityThreshold(SIMILARITY_THRESHOLD)
                                        .build())
                                .build())
                .defaultSystem(system)
                .build() ;
    }

    @GetMapping("/api/rag/filmes")
    public String vendedor (@RequestParam String pergunta) {
        return chatClient
                .prompt()
                .user(pergunta)
                .call()
                .content();
    }

    @GetMapping("/api/rag/filmes-debug")
    public Map<String,List<String>> debug(@RequestParam String pergunta) {
        return Map.of(
                "0.1",similaritySearch(pergunta,0.1),
                "0.3",similaritySearch(pergunta,0.3),
                "0.5",similaritySearch(pergunta,0.5),
                "0.7",similaritySearch(pergunta,0.7),
                "0.9",similaritySearch(pergunta,0.9)
        );
    }

    private List<String> similaritySearch(String pergunta,double limite) {
        return vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(pergunta)
                                .topK(TOP_K)
                                .similarityThreshold(limite)
                                .build())
                .stream()
                .map(doc -> "Score: " + doc.getMetadata().get("distance") + " | " + doc.getText())
                .toList();
    }
}