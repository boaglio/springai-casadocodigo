package com.boaglio.springai.vendotudo.tools;

import java.time.LocalDate;

import org.springframework.ai.tool.annotation.Tool;

public class DateTools {

    @Tool(description = "Get the current year")
    String ano() {
        var year = LocalDate.now().getYear();
        IO.println("Ano atual: %s".formatted(year));
        return String.valueOf(year);
    }

}