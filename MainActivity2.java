package com.example.smarthome10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/*imports for wifi*/

import com.example.Status;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;


public class MainActivity2 extends AppCompatActivity {
    Switch light,door,fan,curtain;
    Button btn_status;

    Socket echoSocket = null;
    DataOutputStream os = null;
    DataInputStream is = null;
    DataInputStream stdIn = new DataInputStream(System.in);
    void lightControl(){
        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    echoSocket = new Socket("192.168.4.1", 80);
                    os = new DataOutputStream(echoSocket.getOutputStream());
                    is = new DataInputStream(echoSocket.getInputStream());
                    os.write("pin=4".getBytes(Charset.forName("UTF-8")));
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        light = findViewById(R.id.switch1);
        door = findViewById(R.id.switch3);
        fan = findViewById(R.id.switch2);
        curtain = findViewById(R.id.switch4);
        btn_status = findViewById(R.id.btn_status);


        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "Light is on",Toast.LENGTH_SHORT).show();
                    try {
                        lightControl();
                    } catch (Exception e) {
                       e.printStackTrace();
                        Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    lightControl();
                    Toast.makeText(MainActivity2.this, "Light is off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "Door is open",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity2.this, "Door is closed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "fan is on",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity2.this, "fan is off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        curtain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "Curtain is drawn",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity2.this, "Curtain is closed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, Status.class));

            }
        });
    }


}
