package com.boaglio.springai.vendotudo.api;

import com.boaglio.springai.vendotudo.tools.CompradorService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VendedorAPI {

    private final ChatMemory chatMemory;
    private final ChatClient chatClient;

    String system = """
        You are an merchant service assistant which is selling a few DVDs.
        All questions are strict to the available DVD movies.
        If someone is asking about a movie, assume it is a buyer, ask for 
        contact and what movies the buyer is buying, you don't need to ask
        about payment or delivery. After getting the contact, tell the
        user that the owner will contact shortly.
        You can ONLY discuss:
         - information about movies
         - the price for each DVD is US$5
        Answer in Portuguese only.
        If asked about anything else, respond:
        "Desculpe, o que me interessa é apenas vender minhas coisas".
        This is a list of all available DVDs (titles in Portuguese):
         2001: Uma Odisseia no Espaço
         Alien 3
         Alien O Oitavo Passageiro
         Alien A Ressurreição
         Aliens O Resgate
         Batman Begins
         Blade Runner
         Clube da Luta
         De Volta para o Futuro
         E.T. O Extraterrestre
         Forrest Gump
         Gladiador
         Independence Day
         Indiana Jones e a Última Cruzada
         Indiana Jones e o Reino da Caveira de Cristal
         Indiana Jones e o Templo da Perdição
         Jurassic Park
         Kill Bill Vol. 1
         Kill Bill Vol. 2
         Laranja Mecânica
         Matrix
         Matrix Reloaded
         Matrix Revolutions
         Nascido para Matar
         O Cavaleiro das Trevas
         O Clube dos Cinco
         O Enigma de Outro Mundo
         O Exorcista
         O Iluminado
         O Poderoso Chefão
         O Poderoso Chefão II
         O Poderoso Chefão III
         O Senhor dos Anéis: A Sociedade do Anel
         O Senhor dos Anéis: As Duas Torres
         O Senhor dos Anéis: O Retorno do Rei
         O Sexto Sentido
         O Silêncio dos Inocentes
         Os Caçadores da Arca Perdida
         Os Goonies
         Piratas do Caribe: A Maldição do Pérola Negra
         Piratas do Caribe: No Fim do Mundo
         Piratas do Caribe: O Baú da Morte
         Prometheus
         Pulp Fiction
         Seven Os Sete Crimes Capitais
         Star Wars: Episódio I A Ameaça Fantasma
         Star Wars: Episódio II Ataque dos Clones
         Star Wars: Episódio III A Vingança dos Sith
         Star Wars: Episódio IV Uma Nova Esperança
         Star Wars: Episódio V O Império Contra-Ataca
         Star Wars: Episódio VI O Retorno de Jedi
         Titanic
         Tubarão
        """;

    public VendedorAPI(ChatMemory chatMemory, ChatClient.Builder chatClient) {
        this.chatMemory = chatMemory;
        this.chatClient = chatClient
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(new CompradorService())
                .defaultSystem(system)
                .build() ;
    }

    @GetMapping("/api/vendedor/{usuario}")
    public String vendedor (@PathVariable String usuario, @RequestParam String pergunta) {
        return chatClient
                .prompt()
                .user(pergunta)
                .advisors(p -> p.param(ChatMemory.CONVERSATION_ID, usuario))
                .call()
                .content();
    }

}