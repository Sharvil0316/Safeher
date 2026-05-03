package com.safety.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Service for sending real-time emergency SOS alerts using Email.
 * Uses JavaMail API with Gmail's free SMTP server.
 */
public class EmailService {

    // IMPORTANT: Replace with your actual Gmail Address & App Password
    public static final String SENDER_EMAIL = "sharvilpatil1441@gmail.com";
    public static final String APP_PASSWORD = "zmprhtddmocysgum";

    /**
     * Sends an Email message to the specified email address.
     */
    public static boolean sendEmail(String recipientEmail, String subject, String messageBody) {
        if ("your.email@gmail.com".equals(SENDER_EMAIL)) {
            System.err.println("[EMAIL ERROR] Please configure your Gmail and App Password in EmailService.java");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

            System.out.println("============================================");
            System.out.println("[EMAIL SERVICE] Real-time SOS Alert Sent!");
            System.out.println("[TO]      : " + recipientEmail);
            System.out.println("============================================");

            return true;
        } catch (MessagingException e) {
            System.err.println("[EMAIL SERVICE] FAILED to send Email to " + recipientEmail);
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

        String subject = "🚨 URGENT: SOS EMERGENCY ALERT from " + userName + " 🚨";
        String message = "Hi " + contactName + ",\n\n"
                + "This is an automated Emergency Alert.\n"
                + userName + " (Phone: " + userPhone + ") has just triggered an SOS!\n\n"
                + "Live Tracking Location: " + locationInfo + "\n\n"
                + "Please check on them immediately or call local emergency services.\n"
                + "Stay safe,\nWomen Safety App Auto-Notifier";

        return sendEmail(contactEmail, subject, message);
    }
}
