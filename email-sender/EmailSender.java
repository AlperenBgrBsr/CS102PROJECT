import javax.mail.*;
import javax.mail.internet.*;

import java.util.*;

public class EmailSender {
    private final static String USERNAME = "bilmartsystem@gmail.com"; 
    private final static String PASSWORD = "tkuqozkfpajskspw"; 
    private static Random random = new Random();

    public static void sendInformationEmailForAdvert(User from, String toName, String toEmail, Advert advert) { // Bu ve aşağıdaki methodun kullanılacağı yerde ikinci bi user yok o yüzden direkt bunları almak parametre olarak daha iyi olabilir
        String fromName = from.getUsername(); 

        String advertTitle = advert.getTitle();
        String fromEmail = from.getEmail();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bilmartsystem@gmail.com", "tkuqozkfpajskspw");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bilmartsystem@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Information mail");
            message.setText("User " + fromName + " with email "+ fromEmail + " has added your advert with the title " + advertTitle);
            Transport.send(message);
            System.out.println("Email sent: " + toName);
        } catch (MessagingException e) {
            System.err.println("Email failed to send!");
            e.printStackTrace();
        }
    }

    public static void sendInformationEmailForContact(User from, String toName, String toEmail) {
        String fromName = from.getUsername();
        String fromEmail = from.getEmail();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bilmartsystem@gmail.com", "tkuqozkfpajskspw");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bilmartsystem@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Information mail");
            message.setText("User " + fromName + " with email "+ fromEmail + " has added you to contacts");
            Transport.send(message);
            System.out.println("Email sent: " + toName);
        } catch (MessagingException e) {
            System.err.println("Email failed to send!");
            e.printStackTrace();
        }
    }

    public static void sendVerificationEmail(User to, int code) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bilmartsystem@gmail.com", "tkuqozkfpajskspw");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bilmartsystem@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.getEmail()));
            message.setSubject("Verification email");
            message.setText("Your verification code for registration: " + code);
            Transport.send(message);
            System.out.println("Email sent: " + to.getUsername());
        } catch (MessagingException e) {
            System.err.println("Email failed to send!");
            e.printStackTrace();
        }
    }
    public static void sendForgotPasswordEmail(User to, int code) {
        Properties props = new Properties();
        
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bilmartsystem@gmail.com", "tkuqozkfpajskspw");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bilmartsystem@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.getEmail()));
            message.setSubject("Verification email");
            message.setText("Your reset code for your password:  " + code);
            Transport.send(message);
            System.out.println("Email sent: " + to.getUsername());
        } catch (MessagingException e) {
            System.err.println("Email failed to send!");
            e.printStackTrace();
        }
    }
}
