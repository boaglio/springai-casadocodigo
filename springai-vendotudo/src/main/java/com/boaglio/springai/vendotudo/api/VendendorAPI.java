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
public class VendendorAPI {

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
         - information about products
         - the price for each DVD is US$5
        Answer in Portuguese only.
        If asked about anything else, respond:
        "Desculpe, o que me interessa é apenas vender minhas coisas".
        This is a list of all available DVDs:
        { [
              {"titulo":"2001: Uma Odisseia no Espaço","ano":1968,"atores":"Keir Dullea, Gary Lockwood"},
              {"titulo":"Laranja Mecânica","ano":1971,"atores":"Malcolm McDowell"},
              {"titulo":"O Poderoso Chefão","ano":1972,"atores":"Marlon Brando, Al Pacino"},
              {"titulo":"O Exorcista","ano":1973,"atores":"Ellen Burstyn, Max von Sydow"},
              {"titulo":"O Poderoso Chefão II","ano":1974,"atores":"Al Pacino, Robert De Niro"},
              {"titulo":"Tubarão","ano":1975,"atores":"Roy Scheider, Richard Dreyfuss"},
              {"titulo":"Star Wars: Episódio IV – Uma Nova Esperança","ano":1977,"atores":"Mark Hamill, Harrison Ford"},
              {"titulo":"Alien – O Oitavo Passageiro","ano":1979,"atores":"Sigourney Weaver"},
              {"titulo":"O Iluminado","ano":1980,"atores":"Jack Nicholson"},
              {"titulo":"Star Wars: Episódio V – O Império Contra-Ataca","ano":1980,"atores":"Mark Hamill"},
              {"titulo":"Os Caçadores da Arca Perdida","ano":1981,"atores":"Harrison Ford"},
              {"titulo":"Blade Runner","ano":1982,"atores":"Harrison Ford, Rutger Hauer"},
              {"titulo":"E.T. – O Extraterrestre","ano":1982,"atores":"Henry Thomas, Drew Barrymore"},
              {"titulo":"O Enigma de Outro Mundo","ano":1982,"atores":"Kurt Russell"},
              {"titulo":"Star Wars: Episódio VI – O Retorno de Jedi","ano":1983,"atores":"Mark Hamill"},
              {"titulo":"Indiana Jones e o Templo da Perdição","ano":1984,"atores":"Harrison Ford"},
              {"titulo":"De Volta para o Futuro","ano":1985,"atores":"Michael J. Fox, Christopher Lloyd"},
              {"titulo":"O Clube dos Cinco","ano":1985,"atores":"Emilio Estevez, Molly Ringwald"},
              {"titulo":"Os Goonies","ano":1985,"atores":"Sean Astin, Josh Brolin"},
              {"titulo":"Aliens – O Resgate","ano":1986,"atores":"Sigourney Weaver"},
              {"titulo":"Nascido para Matar","ano":1987,"atores":"Matthew Modine"},
              {"titulo":"Indiana Jones e a Última Cruzada","ano":1989,"atores":"Harrison Ford, Sean Connery"},
              {"titulo":"O Poderoso Chefão III","ano":1990,"atores":"Al Pacino"},
              {"titulo":"O Silêncio dos Inocentes","ano":1991,"atores":"Jodie Foster, Anthony Hopkins"},
              {"titulo":"Alien 3","ano":1992,"atores":"Sigourney Weaver"},
              {"titulo":"Jurassic Park","ano":1993,"atores":"Sam Neill, Laura Dern"},
              {"titulo":"Forrest Gump","ano":1994,"atores":"Tom Hanks"},
              {"titulo":"Pulp Fiction","ano":1994,"atores":"John Travolta, Samuel L. Jackson"},
              {"titulo":"Seven – Os Sete Crimes Capitais","ano":1995,"atores":"Brad Pitt, Morgan Freeman"},
              {"titulo":"Independence Day","ano":1996,"atores":"Will Smith, Jeff Goldblum"},
              {"titulo":"Alien: A Ressurreição","ano":1997,"atores":"Sigourney Weaver"},
              {"titulo":"Titanic","ano":1997,"atores":"Leonardo DiCaprio, Kate Winslet"},
              {"titulo":"Clube da Luta","ano":1999,"atores":"Brad Pitt, Edward Norton"},
              {"titulo":"Matrix","ano":1999,"atores":"Keanu Reeves, Laurence Fishburne"},
              {"titulo":"O Sexto Sentido","ano":1999,"atores":"Bruce Willis"},
              {"titulo":"Star Wars: Episódio I – A Ameaça Fantasma","ano":1999,"atores":"Liam Neeson"},
              {"titulo":"Gladiador","ano":2000,"atores":"Russell Crowe, Joaquin Phoenix"},
              {"titulo":"O Senhor dos Anéis: A Sociedade do Anel","ano":2001,"atores":"Elijah Wood, Ian McKellen"},
              {"titulo":"Star Wars: Episódio II – Ataque dos Clones","ano":2002,"atores":"Ewan McGregor"},
              {"titulo":"Matrix Reloaded","ano":2003,"atores":"Keanu Reeves"},
              {"titulo":"Matrix Revolutions","ano":2003,"atores":"Keanu Reeves"},
              {"titulo":"O Senhor dos Anéis: As Duas Torres","ano":2002,"atores":"Elijah Wood"},
              {"titulo":"O Senhor dos Anéis: O Retorno do Rei","ano":2003,"atores":"Elijah Wood"},
              {"titulo":"Kill Bill Vol. 1","ano":2003,"atores":"Uma Thurman"},
              {"titulo":"Piratas do Caribe: A Maldição do Pérola Negra","ano":2003,"atores":"Johnny Depp"},
              {"titulo":"Kill Bill Vol. 2","ano":2004,"atores":"Uma Thurman"},
              {"titulo":"Batman Begins","ano":2005,"atores":"Christian Bale"},
              {"titulo":"Star Wars: Episódio III – A Vingança dos Sith","ano":2005,"atores":"Hayden Christensen"},
              {"titulo":"Piratas do Caribe: O Baú da Morte","ano":2006,"atores":"Johnny Depp"},
              {"titulo":"Piratas do Caribe: No Fim do Mundo","ano":2007,"atores":"Johnny Depp"},
              {"titulo":"O Cavaleiro das Trevas","ano":2008,"atores":"Christian Bale, Heath Ledger"},
              {"titulo":"Indiana Jones e o Reino da Caveira de Cristal","ano":2008,"atores":"Harrison Ford"},
              {"titulo":"Prometheus","ano":2012,"atores":"Noomi Rapace, Michael Fassbender"}
            ]
        }
        """;

    public VendendorAPI(ChatMemory chatMemory, ChatClient.Builder chatClient) {
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