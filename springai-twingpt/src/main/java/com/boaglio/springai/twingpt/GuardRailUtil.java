package com.boaglio.springai.twingpt;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuardRailUtil {

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

}