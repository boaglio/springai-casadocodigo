package com.boaglio.springai.vendotudo.api;

import com.boaglio.springai.vendotudo.tools.CompradorService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RAGVendendorAPI {

    private final ChatMemory chatMemory;
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    String system = """
        You are a DVD merchant assistant selling a limited catalog of movies.  
        You will be provided with context containing information about available DVDs retrieved from a vector store.  
        **Use only that context** to answer questions about the movies. 
        - If the user asks about a movie **not present in the context**, respond:  
          "Sorry, that movie is not available in my catalog at the moment."  
        - If the movie is in the context, provide the relevant information (cast, director, plot, etc.) based on the context, then assume the user is a buyer. 
        Ask which movies they want to purchase and request their contact information (do not ask about payment or delivery).  
          Example: "Each DVD costs US$5. Which movies would you like to buy? Please provide your contact so the owner can reach out shortly."  
        - After the user provides contact, confirm that the owner will contact them soon.  
        - The price for each DVD is fixed at US$5 (this is independent of context).  
        - If the user asks about anything unrelated to movies (weather, politics, etc.), respond exactly:  
          "Sorry, I'm only here to sell my DVDs."
        - Answer **always in Portuguese**.  
        - Do not invent any movie details not present in the context.
        """;

    public RAGVendendorAPI(ChatMemory chatMemory,
                           ChatClient.Builder chatClient,
                           @Qualifier("getSimpleVectorStoreMeusFilmes")  VectorStore vectorStore) {
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
        this.chatClient = chatClient
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
                .defaultTools(new CompradorService())
                .defaultSystem(system)
                .build() ;
    }

    @GetMapping("/api/rag/vendedor/{usuario}")
    public String vendedor (@PathVariable String usuario, @RequestParam String pergunta) {
        return chatClient
                .prompt()
                .user(pergunta)
                .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, usuario))
                .call()
                .content();
    }

    @GetMapping("/api/rag/vendedor-debug")
    public List<String> debug(@RequestParam String pergunta) {

        return vectorStore.similaritySearch(
                        SearchRequest.builder()
                                .query(pergunta)
                                .build())
                .stream()
                .map(Document::getText)
                .toList();
    }
}