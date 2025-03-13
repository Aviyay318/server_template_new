package com.app.utils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;




public class ApiEmailProcessor {
        public static void main(String[] args) {
//          sendEmail("byhyhzql@gmail.com","try","try to");
    }
    public static final String SENDER_EMAIL = "kidslearning580@gmail.com";
    public static final String SENDER_PASSWORD = "rvrr dtop tcks yzpz";
    public static final String PERSONAL = "AYRD";

    public static boolean sendEmail(String recipient, String subject, String content) {
        final String host = "smtp.gmail.com";
        final int port = 465;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL,PERSONAL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            String htmlContent = "<html><body>"
                    + "<h3>" + subject + "</h3>"
                    + "<p>" + content.replace("\n", "<br>") + "</p>"
                    + "</body></html>";
            textPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart imagePart = new MimeBodyPart();
            ClassPathResource resource = new ClassPathResource("ae4fc745b13baeb8e8b5a4836af3d288.png");

            try (InputStream inputStream = resource.getInputStream()) {
                Path tempFile = Files.createTempFile("ae4fc745b13baeb8e8b5a4836af3d288", ".png");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                imagePart.attachFile(tempFile.toFile());
            }
            imagePart.setContentID("<profileImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);
            return true;
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage() + e.getCause());
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}