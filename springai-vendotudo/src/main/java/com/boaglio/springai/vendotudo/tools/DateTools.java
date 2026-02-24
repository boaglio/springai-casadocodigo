package com.boaglio.springai.vendotudo.tools;

import java.time.LocalDate;

import org.springframework.ai.tool.annotation.Tool;

public class DateTools {

    @Tool(description = "Get the current date")
    String hoje() {
        var now = LocalDate.now().toString();
        IO.println("Chamou hoje! ");
        return now;
    }

}