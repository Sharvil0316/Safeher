package com.safety.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service for sending real-time emergency SOS alerts using text messages.
 * Uses Fast2SMS API specifically designed to bypass Indian DLT constraints for testing.
 */
public class SMSService {

    // IMPORTANT: Replace this with your actual Fast2SMS API Key
    public static final String FAST2SMS_API_KEY = "pnRbTi8PdYzsQZIKlu6BmH1XhL5orakOe70v2jEyDWcUAtqgFJqNORCB1xYW5P6bTMSlwKyH0UtDmuAr";

    /**
     * Sends an SMS message directly via Fast2SMS REST API.
     */
    public static boolean sendSMS(String phoneNumber, String messageBody) {
        try {
            if ("YOUR_FAST2SMS_API_KEY".equals(FAST2SMS_API_KEY)) {
                System.err.println("[SMS ERROR] Please configure your Fast2SMS API Key in SMSService.java");
                return false;
            }

            // Fast2SMS requires plain 10-digit numbers without the +91 country code
            phoneNumber = phoneNumber.replace("+91", "").trim();
            if (phoneNumber.startsWith("+1")) phoneNumber = phoneNumber.replace("+1", "").trim();
            phoneNumber = phoneNumber.replaceAll("[^0-9]", ""); // Keep only digits

            String urlString = "https://www.fast2sms.com/dev/bulkV2";
            
            // Build the body using URL encoding for the Quick/Test Route
            String body = "route=q" +
                          "&message=" + URLEncoder.encode(messageBody, StandardCharsets.UTF_8) +
                          "&flash=0" +
                          "&numbers=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .header("authorization", FAST2SMS_API_KEY)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("============================================");
            System.out.println("[SMS SERVICE] Fast2SMS Status Code: " + response.statusCode());
            System.out.println("[SMS SERVICE] Response Body: " + response.body());
            System.out.println("============================================");

            // Fast2SMS returns 200 OK and a JSON body containing "return":true on success
            return response.statusCode() == 200 && response.body().contains("\"return\":true");

        } catch (Exception e) {
            System.err.println("[SMS SERVICE] FAILED to send SMS to " + phoneNumber);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends an SOS emergency alert to a contact.
     */
    public static boolean sendSOSAlert(String contactName, String phoneNumber,
                                        String userName, String userPhone,
                                        String latitude, String longitude) {
        String locationInfo;
        if ("Unknown".equals(latitude) || latitude == null || latitude.isEmpty()) {
            locationInfo = "Location unavailable";
        } else {
            locationInfo = "https://www.google.com/maps?q=" + latitude + "," + longitude;
        }

        String message = "🚨 SOS EMERGENCY! 🚨\n"
                + userName + " (" + userPhone + ") pressed SOS. Location: " + locationInfo;

        return sendSMS(phoneNumber, message);
    }
}
