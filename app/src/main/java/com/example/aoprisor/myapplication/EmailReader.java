package com.example.aoprisor.myapplication;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;

/**
 * Created by aoprisor on 30.05.2016.
 */
public class EmailReader extends AsyncTask<Void , Void, String>{
    private String username;
    private String password;
    private final String from = "homecontrollerpy@gmail.com";
    private static String content;
    private boolean deleteFlag;


    public EmailReader(String username, String password){
        this.password = password;
        this.username = username;
        this.deleteFlag = false;
    }

    protected String doInBackground(Void... params){


        Properties props = new Properties();
        props.setProperty("mail.host","imap.gmail.com");
        props.setProperty("mail.port","995");
        props.setProperty("mail.transport.protocol", "imaps");

        try {

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator(){
               protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(username,password);
                }
            });
            Store store = session.getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);
            Message messages[] = inbox.getMessages();
            SearchTerm searchCond = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        javax.mail.Address[] fromWho = message.getFrom();
                        if (fromWho[0].toString().equals(from)){
                            return true;
                        }
                    }catch (javax.mail.MessagingException e){
                        Log.e("Message","Messaging exception caught when trying to filter!");
                    }
                    return false;
                }
            };

            if(!deleteFlag) {
                Message[] filteredMessages = inbox.search(searchCond);
                Log.e("Message", Integer.toString(filteredMessages.length));
                String subject;

                for (int i = 0; i < filteredMessages.length; i++) {
                    subject = filteredMessages[i].getSubject().toString();
                    this.content = subject + 'C';
                    Log.e("Message", filteredMessages[i].getSubject().toString());
                }

                Log.e("Message", "This is the content:" + content);
            }
            else{
                for(Message mes : messages){
                    mes.setFlag(Flags.Flag.DELETED,true);
                    inbox.close(true);
                }
                }

        }
        catch (NoSuchProviderException e) {
            // Restore interrupt status.
            Log.e("Message", "No Such Provider Exception Was Caught");
            Thread.currentThread().interrupt();
        }

        catch (MessagingException e) {
            // Restore interrupt status.
            Log.e("Message", "Messaging Exception Was Caught");
            Log.e("Message",e.fillInStackTrace().toString());
            Thread.currentThread().interrupt();
        }


/*
        catch (IOException e){
            Log.e("Message", "IOException Was Caught");
            Thread.currentThread().interrupt();
        }*/
        return content;
    }

    public void setDeleteFlag(){
        this.deleteFlag = true;
    }

    protected String onPostExecute(Void result) {
        return content;
    }


    public String getContent(){
        return content;
    }

    private void processContent(Multipart content){

    }
}
