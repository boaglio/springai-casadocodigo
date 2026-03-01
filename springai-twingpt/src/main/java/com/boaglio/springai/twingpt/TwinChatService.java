package com.boaglio.springai.twingpt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TwinChatService {

    private final ChatModel phi3ChatModel;
    private final ChatModel ministral3ChatModel;
    private final SegurancaDeConteudoAdvisor segurancaDeConteudoAdvisor;

    public TwinChatService(
            @Qualifier("phi3ChatModel")       ChatModel phi3ChatModel,
            @Qualifier("ministral3ChatModel") ChatModel ministral3ChatModel, SegurancaDeConteudoAdvisor segurancaDeConteudoAdvisor
    ) {
        this.phi3ChatModel = phi3ChatModel;
        this.ministral3ChatModel = ministral3ChatModel;
        this.segurancaDeConteudoAdvisor = segurancaDeConteudoAdvisor;
    }

    public String phi3answer(String pergunta) {
        return phi3ChatModel.call(pergunta);
    }

    public String ministral3Answer(String pergunta) {
        return ministral3ChatModel.call(pergunta);
    }

    public String perguntaSegura(String pergunta) {

        var chatClient = ChatClient
                .builder(ministral3ChatModel)
                .build();

        return chatClient
                .prompt()
                .user(pergunta)
                .advisors(segurancaDeConteudoAdvisor)
                .call()
                .content();
    }

}