package com.boaglio.springai.vendotudo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RAGVendedorEvaluationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String chamarVendedor(String usuario, String pergunta) {
        var url = "http://localhost:" + port
                + "/api/rag/vendedor/" + usuario
                + "?pergunta=" + pergunta;
        return restTemplate.getForObject(url, String.class);
    }

    private String novoUsuario() {
        return "eval-" + UUID.randomUUID();
    }

    @Test
    @DisplayName("Filme fora do catálogo deve retornar mensagem de indisponibilidade")
    void filmeForaDoCatalogoDeveSerRecusado() {

        var pergunta = "Vocês têm o filme 'Interestelar' disponível?";
        var usuario  = novoUsuario();
        var resposta = chamarVendedor(usuario, pergunta);

        assertThat(resposta.toLowerCase())
                .as("A API deveria informar que o filme não está no catálogo. Resposta: %s", resposta)
                .containsAnyOf(
                        "não está disponível",
                        "não temos",
                        "não disponível",
                        "não encontrado no catálogo"
                );
    }

    @Test
    @DisplayName("Pergunta fora do escopo deve ser bloqueada")
    void perguntasOffTopicDevemSerBloqueadas() {

        var usuario  = novoUsuario();
        var pergunta = "Me ajude a escrever um e-mail profissional.";
        var resposta = chamarVendedor(usuario,pergunta );

        assertThat(resposta.toLowerCase())
                .as("A API deveria recusar a pergunta off-topic. Resposta: %s", resposta)
                .containsAnyOf(
                        "somente para vender",
                        "só estou aqui para vender",
                        "apenas aqui para vender",
                        "dvds"
                );
    }

    @Test
    @DisplayName("Preço informado deve ser US$ 5")
    void precoDeveSerCincoDolares() {

        var pergunta = "Quanto custa cada DVD?";
        var usuario  = novoUsuario();
        var resposta = chamarVendedor(usuario, pergunta);

        assertThat(resposta)
                .as("O preço deveria ser US$5. Resposta: %s", resposta)
                .containsAnyOf("US$5", "US$ 5", "5 dólares", "$5", "cinco dólares");
    }

    @Test
    @DisplayName("Após intenção de compra o assistente deve pedir dados de contato")
    void assistenteDeveColetarContatoAposIntencaoDeCompra() {

        var usuario = novoUsuario();

        chamarVendedor(usuario, "Quais filmes vocês têm?");

        var resposta = chamarVendedor(usuario, "Quero comprar um DVD!");

        assertThat(resposta.toLowerCase())
                .as("O assistente deveria solicitar dados de contato. Resposta: %s", resposta)
                .containsAnyOf(
                        "contato",
                        "nome",
                        "e-mail",
                        "email",
                        "telefone",
                        "whatsapp"
                );
    }

}