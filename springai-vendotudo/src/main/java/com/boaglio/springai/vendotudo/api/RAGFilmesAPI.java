package com.boaglio.springai.vendotudo.api;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RAGFilmesAPI {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    SimpleLoggerAdvisor customLogger = SimpleLoggerAdvisor.builder()
            .requestToString(request -> "Request: " + request.prompt())
            .responseToString(response -> "Response: " + response.getResults())
            .build();

    String system = """
        You are an movie expert service assistant.
        All questions are strict to the movies .
        You can ONLY discuss:
         - information about movies
        Answer in Portuguese only.
        If asked about anything else, respond:
        "Desculpe, esse serviço é apenas sobre filmes".
        """;

    public RAGFilmesAPI(ChatClient.Builder chatClient,
                        @Qualifier("getSimpleVectorStoreTop100Filmes") VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .topK(10)
                                        .similarityThreshold(0.4)
                                        .build())
                                .build(),
                        customLogger)
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

    @GetMapping("/api/rag/filmes/debug")
    public List<String> debug(@RequestParam String pergunta) {
        return vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(pergunta)
                                .topK(10)
                                .similarityThreshold(0.7)
                                .build())
                .stream()
                .map(doc -> "Score: " + doc.getMetadata().get("distance") + " | " + doc.getText())
                .toList();
    }

}