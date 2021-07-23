package com.example.fyp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BedControlerByPatient extends AppCompatActivity {
    ImageButton upload, plus, less;
    EditText IP1, IP2;
    TextView txtangle; //show angle
    int angle = 0; //bed's angle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bed_controler_by_patient);
        IP1 = (EditText) findViewById(R.id.IP1);
        IP2 = (EditText) findViewById(R.id.IP2);
        plus = (ImageButton) findViewById(R.id.plus);
        less = (ImageButton) findViewById(R.id.less);
        upload = (ImageButton) findViewById(R.id.upload);
        txtangle = (TextView) findViewById(R.id.angle);
        txtangle.setText("" + angle);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable); //clear cycle
                handler.post(runnable); //start cycle
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angle <= 30) { //bed angele cannot > 40
                    angle += 10;
                    txtangle.setText("" + angle);
                }
            }
        });
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angle >= 10) { ////bed angele cannot < 0
                    angle -= 10;
                    txtangle.setText("" + angle);
                }
            }
        });
        //Create text listener
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(runnable);
                if (IP1.getText().toString().length() == 3 || Integer.parseInt(IP1.getText().toString()) >= 255) { //edit text cannot > 3
                    IP1.clearFocus(); //when edit text > 3, go to next edit text
                    IP2.requestFocus();
                }

                if (IP2.getText().toString().length() == 3 || Integer.parseInt(IP2.getText().toString()) >= 255) { //close keyboard when edit text cannot > 3
                    IP2.clearFocus();
                    IBinder mIBinder = BedControlerByPatient.this.getCurrentFocus().getWindowToken();
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };
        IP1.addTextChangedListener(mTextWatcher); //start text listener
        IP2.addTextChangedListener(mTextWatcher);
    }

    private class BedControlerByPatientInBackground extends AsyncTask<String, String, String> {

        String jsonString1;

        protected String doInBackground(String... params) {
            final String FIP1 = IP1.getText().toString();
            final String FIP2 = IP2.getText().toString();
            final String ANGLE = Integer.toString(angle);
            try {
                HttpURLConnection conn = null;
                URL url = new URL("http://192.168." + FIP1 + "." + FIP2 + "/?led=" + ANGLE); //ip=192.168.?.?   //led = aurdnio columm
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10);
                conn.setConnectTimeout(10);
                conn.setRequestMethod("POST");
                conn.connect();
                // 讀取資料
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                jsonString1 = reader.readLine();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception";
            }
            return jsonString1;
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new BedControlerByPatientInBackground().execute();
            handler.postDelayed(this, 100); //do each 1 second
        }
    };

    @Override
    protected void onStop() { //stop loop when user exit
        // 取消注册
        handler.removeCallbacks(runnable);
        super.onStop();
    }
}
