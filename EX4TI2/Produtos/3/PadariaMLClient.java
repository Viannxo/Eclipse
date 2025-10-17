import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Classe Java que acessa um modelo de Machine Learning
 * publicado como uma API REST em Python (Flask) via requisição POST/JSON.
 */
public class PadariaMLClient {

    // URL do endpoint de previsão da API Flask
    private static final String API_URL = "http://127.0.0.1:5000/predict";

    public static void main(String[] args) {
        
        // 1. Definição dos Dados de Entrada (Features)
        // O modelo espera um array de features no JSON.
        // Exemplo: [Nível_Cliente=2, Hora_Dia=14, Qtd_Produtos_Diferentes=3]
        String jsonInput = """
            {
                "features": [2, 14, 3] 
            }
            """;

        try {
            // 2. Criação do Cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // 3. Construção da Requisição HTTP POST
            HttpRequest request = HttpRequest.newBuilder(URI.create(API_URL))
                    // Define o método POST e o corpo da requisição com o JSON
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    // Define o Content-Type para informar à API que estamos enviando JSON
                    .header("Content-Type", "application/json")
                    .build();

            // 4. Envio da Requisição e Recebimento da Resposta (Blocante/Síncrono)
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Verificação e Processamento da Resposta
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                
                System.out.println("--- RESULTADO DA PREVISÃO ---");
                System.out.println("Status da API: 200 (OK)");
                System.out.println("Resposta Completa (JSON): " + jsonResponse);
                
                // *Nota:* Em um projeto real, você usaria uma biblioteca JSON (como Jackson ou Gson) 
                // para converter de forma segura esta String JSON em um objeto Java.
                
                // Simples extração do valor previsto para demonstração:
                if (jsonResponse.contains("quantidade_prevista")) {
                    String valorBruto = jsonResponse.split(":")[1].replace("}", "").trim();
                    System.out.println("\n** Quantidade de Itens Prevista: " + valorBruto + " **");
                }
                
            } else {
                System.err.println("-------------------------------------");
                System.err.println("ERRO: Falha ao chamar a API do Modelo.");
                System.err.println("Verifique se o servidor Flask está rodando em " + API_URL);
                System.err.println("Status Retornado: " + response.statusCode());
                System.err.println("Corpo da Resposta: " + response.body());
                System.err.println("-------------------------------------");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Exceção ao executar a requisição HTTP: " + e.getMessage());
            System.err.println("Certifique-se de que a API Flask (Passo 2) está ativa.");
        }
    }
}