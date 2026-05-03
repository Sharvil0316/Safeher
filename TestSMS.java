import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TestSMS {
    public static void main(String[] args) {
        try {
            String apiKey = "pnRbTi8PdYzsQZIKlu6BmH1XhL5orakOe70v2jEyDWcUAtqgFJqNORCB1xYW5P6bTMSlwKyH0UtDmuAr";
            String phoneNumber = "8105519075"; // From user's screenshot
            String messageBody = "🚨 SOS EMERGENCY! 🚨\nTest User pressed SOS.";
            
            String urlString = "https://www.fast2sms.com/dev/bulkV2";
            String body = "route=q" +
                          "&message=" + URLEncoder.encode(messageBody, StandardCharsets.UTF_8) +
                          "&flash=0" +
                          "&numbers=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("authorization", apiKey)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
