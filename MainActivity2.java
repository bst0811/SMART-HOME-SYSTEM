package com.example.smarthome10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    Switch light,door,fan,curtain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        light = findViewById(R.id.switch1);
        door = findViewById(R.id.switch3);
        fan = findViewById(R.id.switch2);
        curtain = findViewById(R.id.switch4);


        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "Light is on",Toast.LENGTH_SHORT).show();
                }else{
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
    }
}