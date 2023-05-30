package lk.ijse.carepoint.controller;

import com.sun.javafx.scene.control.Properties;

public class EmailController {
        /*public static void sendMail(String recepient) throws Exception {
            System.out.println("Preparing to send email");

            Properties properties = new Properties();
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enabled", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            String myAccountEmail = "aenpalmoilprocessingpvtltd@gmail.com";
            String password = "ctctxiwckcjstjgx";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });
            Message message = prepareMessage(session, myAccountEmail,recepient);
            Transport.send(message);
            System.out.println("Message sent successfully!");
        }*/
    /*public static void sendTempWarning(String recepient) throws Exception {
        System.out.println("Preparing to send email");

        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enabled", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String myAccountEmail = "soysaquarium@gmail.com";
        String password = "Jaeauxxlmpjptiky";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });
        Message message = prepareMessageHighTemp(session, myAccountEmail,recepient);
        Transport.send(message);
        System.out.println("Message sent successfully!");
    }*/

       /* private static Message prepareMessage(Session session,String myAccountEmail,String recepient) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(myAccountEmail));
                message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
                message.setSubject("Welcome to the AEN Palm Oil Processing (Pvt) Ltd System");
                message.setText("We are glad to have you on board with the AEN Palm Oil Factory Management System.you are now able to manage your factory efficiently and easily.\nIf you have any questions or concerns, please feel free to reach out to us. Our team is always here to support you.\n\nBest regards,\nDilshan Dekumpitiya");
                return message;
            } catch (Exception ex) {
                System.out.println(EmailController.class.getName());
            }
            return null;
        }*/
    /*private static Message prepareMessageHighTemp(Session session,String myAccountEmail,String recepient) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
            message.setSubject("Temperature Warning!");
            message.setText("Temperature is very High! Check your Tanks.");
            return message;
        } catch (Exception ex) {
            System.out.println(EmailController.class.getName());
        }
        return null;
    }*/

}
