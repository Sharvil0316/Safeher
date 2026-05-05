package com.safety.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Service for sending real-time emergency SOS alerts using Email.
 * Uses the SendGrid REST API (HTTP Port 443) to completely bypass Railway's SMTP Firewalls!
 */
public class EmailService {

    // Securely loads the API Key from Railway Environment Variables!
    public static final String SENDGRID_API_KEY = System.getenv("SENDGRID_API_KEY");
    
    // IMPORTANT: This MUST be the exact email address you verified in your SendGrid account!
    public static final String SENDER_EMAIL = "sharvilpatil1441@gmail.com";

    /**
     * Sends an Email message using SendGrid HTTP API.
     */
    public static boolean sendEmail(String recipientEmail, String subject, String messageBody) {
        try {
            URL url = new URL("https://api.sendgrid.com/v3/mail/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + SENDGRID_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Safely escape JSON characters
            String safeMessage = messageBody.replace("\n", "\\n").replace("\"", "\\\"");
            String safeSubject = subject.replace("\"", "\\\"");

            String jsonPayload = "{"
                    + "\"personalizations\": [ { \"to\": [ { \"email\": \"" + recipientEmail + "\" } ] } ],"
                    + "\"from\": { \"email\": \"" + SENDER_EMAIL + "\", \"name\": \"SafeHer SOS\" },"
                    + "\"subject\": \"" + safeSubject + "\","
                    + "\"content\": [ { \"type\": \"text/plain\", \"value\": \"" + safeMessage + "\" } ]"
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 202 || responseCode == 200 || responseCode == 201) {
                System.out.println("============================================");
                System.out.println("[EMAIL SERVICE] SOS Alert Sent via SendGrid API!");
                System.out.println("[TO]      : " + recipientEmail);
                System.out.println("============================================");
                return true;
            } else {
                System.err.println("[EMAIL SERVICE] FAILED to send Email. SendGrid HTTP Response Code: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            System.err.println("[EMAIL SERVICE] FATAL ERROR sending email.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends an SOS emergency alert via Email.
     */
    public static boolean sendSOSAlert(String contactName, String contactEmail,
                                        String userName, String userPhone,
                                        String latitude, String longitude) {
        String locationInfo;
        if ("Unknown".equals(latitude) || latitude == null || latitude.isEmpty()) {
            locationInfo = "Location unavailable";
        } else {
            locationInfo = "https://www.google.com/maps?q=" + latitude + "," + longitude;
        }

        String subject = "URGENT: SOS EMERGENCY ALERT from " + userName;
        String message = "Hi " + contactName + ",\n\n"
                + "This is an automated Emergency Alert.\n"
                + userName + " (Phone: " + userPhone + ") has just triggered an SOS!\n\n"
                + "Live Tracking Location: " + locationInfo + "\n\n"
                + "Please check on them immediately or call local emergency services.\n"
                + "Stay safe,\nWomen Safety App Auto-Notifier";

        return sendEmail(contactEmail, subject, message);
    }
}
