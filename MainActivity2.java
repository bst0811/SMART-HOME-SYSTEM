package com.example.smarthome10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/*imports for wifi*/

import com.example.Status;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;


public class MainActivity2 extends AppCompatActivity {
    Switch light,door,fan,alarm;
    Button btn_status;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;


    Socket echoSocket = null;
    DataOutputStream os = null;
    DataInputStream is = null;
    DataInputStream stdIn = new DataInputStream(System.in);
    void lightControl(){
        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //echoSocket = new Socket("192.168.4.1", 80);
                   echoSocket = new Socket("192.168.43.30", 80);
                    os = new DataOutputStream(echoSocket.getOutputStream());
                    is = new DataInputStream(echoSocket.getInputStream());
                    os.write("pin=9".getBytes(Charset.forName("UTF-8")));
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void DoorControl(){
        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //echoSocket = new Socket("192.168.4.1", 80);
                    echoSocket = new Socket("192.168.43.30", 80);
                    os = new DataOutputStream(echoSocket.getOutputStream());
                    is = new DataInputStream(echoSocket.getInputStream());
                    os.write("pin=6".getBytes(Charset.forName("UTF-8")));
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    void FanControl(){
        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //echoSocket = new Socket("192.168.4.1", 80);

                    echoSocket = new Socket("192.168.43.30", 80);
                    os = new DataOutputStream(echoSocket.getOutputStream());
                    is = new DataInputStream(echoSocket.getInputStream());
                    os.write("pin=5".getBytes(Charset.forName("UTF-8")));
                } catch (Exception e) {
                    Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    void alarmControl(){
        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //echoSocket = new Socket("192.168.4.1", 80);
                    echoSocket = new Socket("192.168.43.30", 80);
                    os = new DataOutputStream(echoSocket.getOutputStream());
                    is = new DataInputStream(echoSocket.getInputStream());
                    os.write("pin=8".getBytes(Charset.forName("UTF-8")));
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
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        light = findViewById(R.id.switch1);
        door = findViewById(R.id.switch3);
        fan = findViewById(R.id.switch2);
        alarm = findViewById(R.id.switch4);
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
                    try {
                        DoorControl();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    DoorControl();
                    Toast.makeText(MainActivity2.this, "Door is closed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "fan is on",Toast.LENGTH_SHORT).show();
                    try {
                        FanControl();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FanControl();
                    Toast.makeText(MainActivity2.this, "fan is off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity2.this, "Alarm is on",Toast.LENGTH_SHORT).show();
                    try {
                        alarmControl();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity2.this, e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity2.this, "Alarm is off",Toast.LENGTH_SHORT).show();
                    alarmControl();
                }
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, Status.class));

            }
        });


        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.mic);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if( data.get(0).equals("door open")){
                    editText.setText(data.get(0));
                door.toggle();
                }else if(data.get(0).equals("door close")){
                    editText.setText(data.get(0));
                    door.toggle();
                }else if(data.get(0).equals("light on")){
                    editText.setText(data.get(0));
                    light.toggle();
                }
                else if(data.get(0).equals("light off")){
                    editText.setText(data.get(0));
                    light.toggle();
                }
                else if(data.get(0).equals("fan on")){
                    editText.setText(data.get(0));
                    fan.toggle();
                }else if(data.get(0).equals("fan off")){
                    editText.setText(data.get(0));
                    fan.toggle();
                }
                else if(data.get(0).equals("alarm on")){
                    editText.setText(data.get(0));
                    alarm.toggle();
                }
                else if(data.get(0).equals("alarm off")){
                    editText.setText(data.get(0));
                    alarm.toggle();
                }
                else{
                    editText.setText("wrong command! Try these door open/close, light on/off, fan on/off, alarm on/off");
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.mic);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


}
