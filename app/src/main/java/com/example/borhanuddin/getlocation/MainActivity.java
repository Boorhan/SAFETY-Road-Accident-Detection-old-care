package com.example.borhanuddin.getlocation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    boolean flag=true;
    CountDownTimer countDownTimer;
    EditText numberEditText;
    Button sendButton;
    TextView valueTextView,massageText;

    private SensorManager sm;
    private float acelVal; // current acceleration including gravity
    private float acelLast; // last acceleration including gravity
    private float shake; // acceleration apart from gravity

    TextView graphViewTextView,nearbyTextView,contactsTextView,sensibilityTextView;

    public void SendSms(){
        String token = "8182099eda1cc8d7c4b8c7a77243385e";
        Intent intent = getIntent();
        String acr = intent.getStringExtra("Acr");
        String Lng = intent.getStringExtra("Lng");
        String Lat = intent.getStringExtra("Lat");

        //Single or Multiple mobiles numbers separated by comma
        Intent i = getIntent();
        String easyPuzzle = i.getStringExtra("puzzle");
        //Single or Multiple mobiles numbers separated by comma
        String to = easyPuzzle;
                to+=",01952515672";

        //Your message to send, Add URL encoding here.
        String textmessage = "Your friend is in trouble at " + "https://www.google.com/maps/@"+Lat+","+Lng+","+acr;

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        //encode the message content
        String encoded_message=URLEncoder.encode(textmessage);
        String apiUrl="http://api.greenweb.com.bd/api.php?";

        StringBuilder sgcPostContent= new StringBuilder(apiUrl);
        sgcPostContent.append("token="+token);
        sgcPostContent.append("&to="+to);
        sgcPostContent.append("&message="+encoded_message);

        apiUrl = sgcPostContent.toString();
        try {
            //prepare connection
            myURL = new URL(apiUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //read the output
            String output;
            while ((output = reader.readLine()) != null)
                //print output
                Toast.makeText(getApplicationContext(), "It's Done: "+output, Toast.LENGTH_SHORT).show();

            //Close connection
            reader.close();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public int shaketimer,temp=0;
    public void ShowDialog()
    {
        final android.app.AlertDialog.Builder popDialog = new android.app.AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(100);

        seek.setProgress(temp);

        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Set Sensibility ");
        popDialog.setView(seek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
//Do something here with new value
                temp=progress;
                if(progress>10 && progress<90)
                {
                    valueTextView.setText("Average Sensibility-"+progress);
                    valueTextView.setTextColor(Color.WHITE);
                    massageText.setText("Obtainable!");
                    massageText.setTextColor(Color.WHITE);
                }
                else if(progress>90 && progress<=100)
                {
                    valueTextView.setText("Low Sensibility-"+progress);
                    valueTextView.setTextColor(Color.RED);
                    massageText.setText("Impossible to obtain");
                    massageText.setTextColor(Color.RED);
                }
                else if(progress>0 && progress<=10)
                {
                    valueTextView.setText("High Sensibility-"+progress);
                    valueTextView.setTextColor(Color.GREEN);
                    massageText.setText("Easy to obtain");
                    massageText.setTextColor(Color.GREEN);
                }
            }
            public void onStartTrackingTouch(SeekBar arg0) {
// TODO Auto-generated method stub

            }
            public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
            }
        });
// Button OK
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        shaketimer=temp;
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        shake = 0.00f;
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;

        valueTextView=(TextView)findViewById(R.id.sensibilityValue);
        massageText=(TextView)findViewById(R.id.Massage);

        nearbyTextView=(TextView)findViewById(R.id.nearbyTextView);
        nearbyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Nearby_activity.class);
                startActivity(intent);
            }
        });

        contactsTextView=(TextView)findViewById(R.id.contactsTextView);

        contactsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Contacts_activity.class);
                startActivity(intent);
            }
        });

        sensibilityTextView=(TextView)findViewById(R.id.sensibilityTextView);

        sensibilityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();

            }
        });

        graphViewTextView=(TextView)findViewById(R.id.graphViewTextView);

        graphViewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Graph_activity.class);
                startActivity(intent);
            }
        });


        StrictMode.ThreadPolicy threadPolicy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }


    private final SensorEventListener sensorListener=new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {


            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal-acelLast;
            shake = shake * 0.9f + delta; // perform low-cut filter
            int tech=12;
            if(shaketimer!=0){
                tech=shaketimer;
            }
            if (shake >tech) {

                if(flag){

                    flag=false;
                    final boolean[] sending = {true};
                    Toast.makeText(MainActivity.this, "Shake has been detected !!!", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm_02);
                    mediaPlayer.start();


                    final DialogInterface dialog;
                    AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
                    altdial.setMessage("Sending Loaction?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sending[0] =false;
                                    mediaPlayer.stop();
                                    flag=true;
                                    SendSms();
                                }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sending[0] =false;
                                mediaPlayer.stop();
                                flag=true;
                            }
                        });
                    final AlertDialog alert = altdial.create();
                    alert.setTitle("");
                    alert.show();
                    countDownTimer = new CountDownTimer(5000 + 100, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {

                            mediaPlayer.stop();
                            alert.cancel();
                            if(sending[0]){
                                SendSms();
                            }

                     }
                    }.start();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
