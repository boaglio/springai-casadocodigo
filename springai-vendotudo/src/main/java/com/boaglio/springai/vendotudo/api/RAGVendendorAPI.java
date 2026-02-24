package com.boaglio.springai.vendotudo.api;

import com.boaglio.springai.vendotudo.tools.CompradorService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RAGVendendorAPI {

    private final ChatMemory chatMemory;
    private final ChatClient chatClient;

    String system = """
        You are an merchant service assistant which is selling a few DVDs.
        All questions are strict to the available DVD movies.        
        If someone is asking about a movie, assume it is a buyer, ask for 
        contact and what movies the buyer is buying, you don't need to ask
        about payment or delivery. After getting the contact, tell the
        user that the owner will contact shortly.
        You can ONLY discuss:
         - information about products
         - the price for each DVD is US$5
        Answer in Portuguese only.
        If asked about anything else, respond:
        "Desculpe, o que me interessa é apenas vender minhas coisas". 
        """;

    public RAGVendendorAPI(ChatMemory chatMemory,
                           ChatClient.Builder chatClient,
                           @Qualifier("getSimpleVectorStoreMeusFilmes")  VectorStore vectorStore) {
        this.chatMemory = chatMemory;
        this.chatClient = chatClient
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(), QuestionAnswerAdvisor.builder(vectorStore).build())
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

}