package com.app.utils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
//import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;




public class ApiEmailProcessor {
    public static void main(String[] args) {
        sendEmail("ramrevivo0@gmail.com","try","try to");
    }
    public static final String SENDER_EMAIL = "kidslearning580@gmail.com";
    public static final String SENDER_PASSWORD = "rvrr dtop tcks yzpz";
    public static final String PERSONAL = "AYRD";

    public static boolean sendEmail(String recipient, String subject, String otpCode) {
        Properties properties = getProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, PERSONAL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            File resizedImage = resizeImage();

            MimeBodyPart textPart = getMimeBodyPart(subject, otpCode);

            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile(resizedImage);
            imagePart.setContentID("<profileImage>");
            imagePart.setDisposition(MimeBodyPart.INLINE);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent to " + recipient);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static File resizeImage() throws Exception {
        ClassPathResource resource = new ClassPathResource("boy_otp.png");
        Path tempFile = Files.createTempFile("boy_otp_resized", ".png");
//        try (InputStream inputStream = resource.getInputStream()) {
//            Thumbnails.of(inputStream)
//                    .size(550, 550)
//                    .outputFormat("png")
//                    .toFile(tempFile.toFile());
//        }
        return tempFile.toFile();
    }
    private static Properties getProperties() {
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
        return properties;
    }

    private static MimeBodyPart getMimeBodyPart(String subject, String otp) throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        String htmlContent = """
        <html dir="rtl">
        <body style="font-family: 'Fredoka', Arial, sans-serif; text-align: center; direction: rtl; background-color: #ffffff; padding: 0; margin: 0;">
            <div style="max-width: 600px; margin: auto;">
                <img src="cid:profileImage" alt="OTP" style="width: 100%; max-width: 550px; display: block; margin: 0 auto;" />
                <h1 style="font-size: 36px; color: #222; margin-top: 20px;">"""+subject+"""
                </h1>
                <p style="font-size: 32px; font-weight: bold; color: #007bff;">""" + otp + """
            </p>
            </div>
        </body>
        </html>
        """;

        textPart.setContent(htmlContent, "text/html; charset=UTF-8");
        return textPart;
    }


}