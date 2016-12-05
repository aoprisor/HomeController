package com.example.aoprisor.myapplication;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.login);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                startHomeControllerActivity();
            }catch (Exception e){
                    Log.e("Message", "Exception:"+e.fillInStackTrace().toString());
                }
            }
        });
    }

    public void startHomeControllerActivity() {

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        Intent intent = new Intent(this, HomeControllerActivity.class);

        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        startActivity(intent);

    }

}
