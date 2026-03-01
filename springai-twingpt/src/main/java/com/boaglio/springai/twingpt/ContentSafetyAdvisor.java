package com.boaglio.springai.twingpt;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class ContentSafetyAdvisor implements CallAdvisor {

    private final GuardRailUtil guardRailUtil;

    public ContentSafetyAdvisor(GuardRailUtil guardRailUtil) {
        this.guardRailUtil = guardRailUtil;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        var userInput = request.prompt().getUserMessage().getText();
        if (guardRailUtil.containsProhibitedContent(userInput)) {
            throw new GuardrailViolationException("Desculpe, identificamos conteúdo inapropriado na pergunta");
        }

        var response = chain.nextCall(request);

        if (guardRailUtil.containsSensitiveData(response.chatResponse().getResult().getOutput().getText())) {
            throw new GuardrailViolationException("Desculpe, identificamos conteúdo sensível na resposta");
        }

        return response;
    }

    public String getName() { return "ContentSafetyAdvisor"; }

    @Override
    public int getOrder() { return Ordered.HIGHEST_PRECEDENCE; }
}