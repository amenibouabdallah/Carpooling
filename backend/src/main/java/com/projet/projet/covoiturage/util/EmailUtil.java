package com.projet.projet.covoiturage.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**public class EmailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "58690970moiii@gmail.com";
    private static final String SMTP_PASSWORD = "ngrzxqhpjjlozwip";

    public static void sendVerificationEmail(String email, String verificationCode) {
        // SMTP server configuration
        String smtpHost = "smtp.gmail.com"; // Replace with your SMTP host
        String smtpPort = "587"; // Common for TLS
        String smtpUsername = "amenallah.boughalmi@etudiant-fst.utm.tn"; // Replace with your email
        String smtpPassword = "14776035"; // Replace with your email password

        // Email content
        String subject = "Verify Your Email Address";
        String messageBody = "Hello,\n\n" +
                "Thank you for registering. Please verify your email address using the code below:\n\n" +
                verificationCode + "\n\n" +
                "If you did not register, please ignore this email.\n\n" +
                "Thank you,\nYour App Team";

        // Configure properties for the email
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a mail session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send the email
            Transport.send(message);
            System.out.println("Verification email sent to " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send verification email to " + email);
        }
    }
}**/

public class EmailUtil {
    private static final String EMAIL = "58690970moiii@gmail.com";
    private static final String PASSWORD = "ngrzxqhpjjlozwip";

    public static void sendEmail(String recipient, String subject, String messageText) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageText);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email.");
        }
    }
}

