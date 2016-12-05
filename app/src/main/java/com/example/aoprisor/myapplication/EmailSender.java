package com.example.aoprisor.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by aoprisor on 27.05.2016.
 */
/*Aplicatie pentru trimis mail folosind serverul smtp de la Google
Portul 587 TLS
Portul 465 SSL
Necesita autentificare
clasa EmailSender extinde AsyncTask pentru a putea realiza procesele de retea
     in background fara sa afecteze thred-ul principal
 */



public class EmailSender extends AsyncTask<Void, Void, Void> {
    private String username;
    private String sendTo;
    private String password;
    private Properties properties;
    private String subject;
    private String body;

    //constructorul clasei
    public EmailSender(String username, String password, String sendTo) {
        this.username = username;
        this.password = password;
        this.sendTo = sendTo;
        Properties props = new Properties();

        //Setarea proprietatilor pentru a se putea realiza conexiunea la server
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        properties = props;

    }

    public void setContent(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    //metoda se apeleaza cu obj.execute()
    protected Void doInBackground(Void... urls) {

        try {
            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });


            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.addRecipient(MimeMessage.RecipientType.TO,
                    new InternetAddress(sendTo));
            msg.setSubject(subject);
            msg.setText(body);


            Transport transport = session.getTransport("smtp");
            transport.connect(username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (Exception e) {
            Log.e("Message", "An error has occured sending the message");

        }
        return null;
    }


}
