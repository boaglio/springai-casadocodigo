package com.boaglio.springai.comedyclub;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class PiadaTechAPI {

    @Autowired
    private ChatClient.Builder chatClient;

    @GetMapping("/api/nova-piada-tech")
    public String process() {

        var prompt = "Conte uma piada de tecnologia";

        return chatClient
                .build()
                .prompt(prompt)
                .call()
                .content();
    }

}