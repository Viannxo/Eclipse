import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AzureCognitiveClient {
    private static final String ENDPOINT = "https://padaria-server-key1.cognitiveservices.azure.com/";
    private static final String KEY = System.getenv("AZURE_TEXT_KEY");
    private static final String PATH = "text/analytics/v3.0/sentiment";

    public static void main(String[] args) throws Exception {
        if (KEY == null || KEY.isEmpty()) {
            System.err.println("Set AZURE_TEXT_KEY environment variable with your subscription key.");
            System.exit(1);
        }

        String text = "O pão de queijo da padaria está delicioso! É o melhor que já comi.";

        String body = "{ \"documents\": [ { \"id\": \"1\", \"language\": \"pt\", \"text\": "
                + "\"" + text.replace("\"", "\\\"") + "\" } ] }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT + PATH))
                .header("Content-Type", "application/json")
                .header("Ocp-Apim-Subscription-Key", KEY)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        String json = response.body();

        System.out.println("Status HTTP: " + status);

        if (status >= 200 && status < 300) {
            Pattern pattern = Pattern.compile("\"sentiment\"\\s*:\\s*\"(positive|negative|neutral)\"");
            Matcher matcher = pattern.matcher(json);

            if (matcher.find()) {
                String sentiment = matcher.group(1);
                System.out.println("Sentimento detectado: " + sentiment.toUpperCase());
            } else {
                System.out.println("Não foi possível detectar o sentimento na resposta.");
            }
        } else {
            System.out.println("Erro: " + json);
        }
    }
}
