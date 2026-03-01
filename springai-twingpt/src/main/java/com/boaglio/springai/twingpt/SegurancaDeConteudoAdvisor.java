package com.boaglio.springai.twingpt;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class SegurancaDeConteudoAdvisor implements CallAdvisor {

    private final GuardRailUtil guardRailUtil;

    public SegurancaDeConteudoAdvisor(GuardRailUtil guardRailUtil) {
        this.guardRailUtil = guardRailUtil;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        var sanitizedInput = guardRailUtil.sanitizeInput(request.prompt().getUserMessage().getText());

        if (sanitizedInput.contains(GuardRailUtil.CONTEUDO_FILTRADO)) {
            throw new GuardrailViolationException("Desculpe, identificamos acesso ilegal na pergunta");
        }

        if (guardRailUtil.containsProhibitedContent(sanitizedInput)) {
            throw new GuardrailViolationException("Desculpe, identificamos conteúdo inapropriado na pergunta");
        }

        var response = chain.nextCall(request);

        if (guardRailUtil.containsSensitiveData(response.chatResponse().getResult().getOutput().getText())) {
            throw new GuardrailViolationException("Desculpe, identificamos conteúdo sensível na resposta");
        }

        return response;
    }

    public String getName() { return "SegurancaDeConteudoAdvisor"; }

    @Override
    public int getOrder() { return Ordered.HIGHEST_PRECEDENCE; }
}