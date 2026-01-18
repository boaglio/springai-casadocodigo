import java.util.Random;

public class MiniLLMLeaoVsTigre {

    static final int EPOCHS = 100_000;
    static final double LEARNING_RATE = 0.01;

    static final int TOKEN_LEAO = 0;
    static final int TOKEN_TIGRE = 1;

    static double pesoLeao = 0.0;
    static double pesoTigre = 0.0;

    static Random random = new Random();

    void main() {

        for (int i = 0; i < EPOCHS; i++) {

            var tokenReal = random.nextBoolean() ? TOKEN_LEAO : TOKEN_TIGRE;

            // === Forward pass ===
            var probs = softmax(pesoLeao, pesoTigre);

            // === Erro (loss/perda) ===
            // Cross-entropy simplificada
            var erroLeao  = probs[0] - (tokenReal == TOKEN_LEAO ? TOKEN_TIGRE : TOKEN_LEAO);
            var erroTigre = probs[1] - (tokenReal == TOKEN_TIGRE ? TOKEN_TIGRE :TOKEN_LEAO);

            // === Back propagation (gradiente descendente) ===
            pesoLeao  -= LEARNING_RATE * erroLeao;
            pesoTigre -= LEARNING_RATE * erroTigre;

            if (i % 10_000 == 0) {
                IO.println(
                    "Iteração %d | P(Leão)=%.4f P(Tigre)=%.4f%n "
                    .formatted(i, probs[0], probs[1])
                );
            }
        }

        var finalProbs = softmax(pesoLeao, pesoTigre);

        IO.println("Peso Leão: " + pesoLeao);
        IO.println("Peso Tigre: " + pesoTigre);
        IO.println("Probabilidade Leão: " + finalProbs[0]);
        IO.println("Probabilidade Tigre: " + finalProbs[1]);
    }

    // transforma esses valores brutos 
    // em probabilidades comparáveis, que somam 1.
    static double[] softmax(double a, double b) {
        var expA = Math.exp(a);
        var expB = Math.exp(b);
        var sum = expA + expB;
        return new double[]{expA / sum, expB / sum};
    }
}
