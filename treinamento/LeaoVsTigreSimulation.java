import java.util.Random;

public class LeaoVsTigreSimulation {

    void main() {
        
        var totalSimulacoes = 100_000;
        var vitoriasLeao = 0;
        var vitoriasTigre = 0;

        var random = new Random();

        for (var i = 0; i < totalSimulacoes; i++) {
            var leaoGanha = random.nextBoolean();

            if (leaoGanha) {
                vitoriasLeao++;
            } else {
                vitoriasTigre++;
            }
        }

        var taxaLeao = vitoriasLeao / (double) totalSimulacoes;
        var taxaTigre = vitoriasTigre / (double) totalSimulacoes;

        IO.println("----- Resultado do Treino -----");
        IO.println("Total de simulações: " + totalSimulacoes);
        IO.println("Vitórias do Leão : " + vitoriasLeao);
        IO.println("Vitórias do Tigre: " + vitoriasTigre);
        IO.println("Probabilidade estimada  (Leão vence): " + taxaLeao);
        IO.println("Probabilidade estimada (Tigre vence): " + taxaTigre);
    }
}