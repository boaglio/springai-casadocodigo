package com.boaglio.springai.vendotudo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class CompradorService {

    @Tool(description = "Stores buyer information")
    public void comprador(@ToolParam(description = "the buyer contact")  String contato,
                          @ToolParam(description = "products that buyer wants") String itens) {
        IO.println("---------------");
        IO.println("Contato do comprador: %s".formatted(contato));
        IO.println("Itens do comprador: %s".formatted(itens));
    }

}
