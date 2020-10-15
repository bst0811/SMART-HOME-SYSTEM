package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarthome10.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;


import 	java.net.URLConnection;

public class Status extends AppCompatActivity {

    private ProgressBar progress;
    public static final int TIMEOUT = 10000;
    public static final int CONN_TIMEOUT = 10000;

    TextView temp, smoke_status, door_status, bulb_status, led_status, fan_status, buzzer_status, motion_status;
    Button btn;
    private static final String TAG = Status.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        temp = findViewById(R.id.tv_temp);
        //temp.setText("23");
        smoke_status = findViewById(R.id.smokeTv);
        bulb_status = findViewById(R.id.bulbTv);
        door_status = findViewById(R.id.doorTv);
        fan_status = findViewById(R.id.fanTv);
        buzzer_status = findViewById(R.id.alarmTv);
        motion_status = findViewById(R.id.motionTv);
        btn = findViewById(R.id.btn_refresh);

        /*temp.setText("28*c");
        smoke_status.setText("None detected");
        bulb_status.setText("On");
        door_status.setText("Locked");
        fan_status.setText("Off");
        buzzer_status.setText("Off");
        motion_status.setText("None detected");*/
        String a;

        progress =findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);

        new Async().execute();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new Async().execute();
                }
                catch (Exception e){
                    Toast.makeText(Status.this, "Failed to connect", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }

                Toast.makeText(Status.this, "loading", Toast.LENGTH_SHORT);
            }
        });

    }

    private class Async extends AsyncTask<Void,String,String>{

        HttpURLConnection connection;
        URL url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            progress.setVisibility(View.VISIBLE);
            try{
                url =new URL("http://192.168.4.1/");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG,"Malformed URL");
                return e.toString();
            }
            try{

                connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(CONN_TIMEOUT);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setUseCaches(false);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try{

                    InputStream inputStream = connection.getInputStream();
                    //InputStream errorStream = connection.getErrorStream();
                    BufferedReader bufferedReader =new BufferedReader( new InputStreamReader(inputStream));


                    // BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder jResult = new StringBuilder();
                    StringBuilder jError = new StringBuilder();
                    String line;
                    String error;

                    while((line = bufferedReader.readLine())!=null){
                        jResult.append(line);
                    }

                    /*while((error = errorReader.readLine()) != null){
                        jError.append(error);
                    }*/

                    //Log.d(TAG,jError.toString());
                    return (jResult.toString());

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }finally {
                connection.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if(s== null){
                Log.d(TAG,"String is null");
            }
            try{
                JSONObject jsonObject = new JSONObject(s);

                temp.setText(jsonObject.getString("temp"));
                smoke_status.setText(jsonObject.getString("smoke"));

                bulb_status.setText(jsonObject.getString("light"));
                door_status.setText(jsonObject.getString("door"));
                fan_status.setText(jsonObject.getString("fan"));
                if(jsonObject.getString("smoke") == "detected"){
                    buzzer_status.setText("on");
                }else {
                    buzzer_status.setText("off");
                }

                motion_status.setText(jsonObject.getString("motion"));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG,"JSON Exeption");
            }
        }
    }



}