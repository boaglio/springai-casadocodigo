package com.boaglio.springai.twingpt;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TwinChatService {

    private final ChatModel phi3ChatModel;
    private final ChatModel ministral3ChatModel;

    public TwinChatService(
        @Qualifier("phi3ChatModel")       ChatModel phi3ChatModel,
        @Qualifier("ministral3ChatModel") ChatModel ministral3ChatModel
    ) {
        this.phi3ChatModel = phi3ChatModel;
        this.ministral3ChatModel = ministral3ChatModel;
    }

    public String phi3answer(String prompt) {
        return phi3ChatModel.call(prompt);
    }

    public String ministral3Answer(String prompt) {
        return ministral3ChatModel.call(prompt);
    }
}