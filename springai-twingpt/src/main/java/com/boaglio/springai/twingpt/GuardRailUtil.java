package com.boaglio.springai.twingpt;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GuardRailUtil {

    static final String CONTEUDO_FILTRADO = "[filtered]]";
    static final List<String> BAD_WORDS = List.of("caramba","caraca");
    static final List<String> SENSITIVE_DATA_WORDS = List.of("cpf","pix");

    public boolean containsProhibitedContent(String text) {

        for (String word : BAD_WORDS) {
            if (text.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public boolean containsSensitiveData(String text) {

        for (String word : SENSITIVE_DATA_WORDS) {
            if (text.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;

    }

    public String sanitizeInput(String userInput) {

        if (Objects.isNull(userInput)) return "";

        return userInput
                // Limitar tamanho da entrada
                .substring(0, Math.min(userInput.length(), 2000))
                // Removendo mudança de prompts comuns
                .replaceAll("(?i)(ignore (previous|above|all) instructions?)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(ignore instruções)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(ignore instrucoes)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(ignore as instruções)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(ignore as instrucoes)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(you are now|act as|pretend to be|forget your)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(você é agora|aja como|finja ser|esqueça|esqueca)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(system prompt|ignore your training)", CONTEUDO_FILTRADO)
                .replaceAll("(?i)(sistema|ignore seu treinamento|ignore treinamento)", CONTEUDO_FILTRADO)
                .trim();
    }

}