package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class Splash extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = findViewById(R.id.imageS);
        TextView namaLogin = findViewById(R.id.namaLogin);
        TextView deskripsiBawah = findViewById(R.id.deskripsiBawah);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        String fullName = user.get(sessionManager.FULLNAME);

        namaLogin.setText("Hi, "+fullName+" :)");

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        namaLogin.startAnimation(animation);
        deskripsiBawah.startAnimation(animation);

        Thread timer = new Thread(){

            @Override
            public void run(){
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }
}
