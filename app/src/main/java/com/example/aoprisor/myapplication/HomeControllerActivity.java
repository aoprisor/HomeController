package com.example.aoprisor.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeControllerActivity extends AppCompatActivity {

    private String username="  ";
    private String password="  ";
    private final String sendTo = "homecontrollerpy@gmail.com";
    private TextView temperature;
    private EmailReader readerObject;
    private static char status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_controller);
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        final TextView buttonStatus = (TextView) findViewById(R.id.textView3);

        final Button temperatureButton = (Button) findViewById(R.id.temperatureButton);
        final Button alertButton = (Button) findViewById(R.id.alertButton);
        final Button updateButton = (Button) findViewById(R.id.updateButtonId);
        temperature = (TextView) findViewById(R.id.temperatureView);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == 0){
                    controlAlert("startAlert");
                    status = 1;
                    buttonStatus.setText("ON");
                    alertButton.setText("Turn off");
                }
                else{
                    controlAlert("stopAlert");
                    status = 0;
                    buttonStatus.setText("OFF");
                    alertButton.setText("Turn on");
                }
            }
        });

        temperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                EmailReader deletEmails = new EmailReader(username,password);
                deletEmails.setDeleteFlag();
                deletEmails.execute();
                temperature.setText("Request sent!");
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temperature.setText("Updateing...");
                getTemperature();

            }
        });


    }


    public void getTemperature(){
        AsyncTask<Void, Void, String> temperature;
        EmailReader tempObject = new EmailReader(username,password);
        temperature = tempObject.execute();
        try {

            this.temperature.setText(temperature.get());
        }catch (Exception e){
            Log.e("Mesaje","Nu se citesc mailurile");
        }
    }


    public void sendRequest(){
        EmailSender tempEmail = new EmailSender(username,password,sendTo);
        tempEmail.setContent("Command","sendTemperature");
        tempEmail.execute();
    }

    public void controlAlert(String command){
        EmailSender alertEmail = new EmailSender(username,password,sendTo);
        alertEmail.setContent("Command",command);
        alertEmail.execute();
    }

}
