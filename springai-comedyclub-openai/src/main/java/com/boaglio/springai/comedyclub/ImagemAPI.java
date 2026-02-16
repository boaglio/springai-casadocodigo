package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImagemAPI {

    private final ChatClient.Builder chatClient;
    private Media imagem;

    public ImagemAPI(ChatClient.Builder chatClient) {

        this.chatClient = chatClient;

        this.imagem =  Media.builder()
                        .id("cc")
                        .mimeType(MimeTypeUtils.IMAGE_PNG)
                        .data(new ClassPathResource("img/cc.jpg"))
                        .build();
    }

    @GetMapping("/api/descreva-imagem")
    String descreverImagemCasaDoCodigo() {
        UserMessage um = UserMessage.builder().text("""
                Explain in Portuguese what do you see on the image in the input prompt.
                """)
                .media(imagem)
                .build();

        return this.chatClient
                .build()
                .prompt(new Prompt(um))
                .call()
                .content();
    }

}