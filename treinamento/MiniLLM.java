import java.util.*;
import java.util.stream.IntStream;
 
public class MiniLLM {
 
void main() {

    var EPOCHS = 20;
    var trainingData = "ABABAB";
    
    // A=0, B=1. 
    var weights = new double[2][2]; 

    for (int epoch = 0; epoch < EPOCHS; epoch++) {
        for (int i = 0; i < trainingData.length() - 1; i++) {
            // Entra 'A' (0), Alvo é 'B' (1)
            var inputIdx = (trainingData.charAt(i) == 'A') ? 0 : 1;
            var targetIdx = (trainingData.charAt(i + 1) == 'A') ? 0 : 1;

            // --- FORWARD (Previsão) ---
            var logits = weights[inputIdx];
            var probs = softmax(logits);

            // --- BACKPROP (Ajuste) ---
            for (int k = 0; k < 2; k++) {
                var targetVal = (k == targetIdx) ? 1.0 : 0.0;
                var gradient = probs[k] - targetVal;
                
                // Atualiza o peso: Novo = Velho - (TaxaAprendizado * Erro)
                weights[inputIdx][k] -= 0.1 * gradient;
            }
        }
    }

    // Teste 1: Se entrar 'A', o que vem depois?
    var resultA = softmax(weights[0]);
    IO.println("Iterações: " + EPOCHS + " | Entrada 'A' -> Previsão 'A': "+fmt(resultA[0])+"% | Previsão 'B': "+fmt(resultA[1])+"%");
    
    // Teste 2: Se entrar 'B', o que vem depois?
    var resultB = softmax(weights[1]);
    IO.println("Iterações: " + EPOCHS + " | Entrada 'B' -> Previsão 'A': "+fmt(resultB[0])+"% | Previsão 'B': "+fmt(resultB[1])+"%");
}

 double[] softmax(double[] logits) {
    var exp0 = Math.exp(logits[0]);
    var exp1 = Math.exp(logits[1]);
    var sum = exp0 + exp1;
    return new double[] { exp0 / sum, exp1 / sum };
 }

 String fmt(double val) {
    return String.format("%.1f", val * 100);
 }
}