package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Notification_closed extends AppCompatActivity {
    TextView Data0;
    String id, TextData, Data;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_closed);
        Data0 = (TextView) findViewById(R.id.textView0);
        back = (Button) findViewById(R.id.back);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        TextData = bundle.getString("Text");
        Data = bundle.getString("Data");
        Data0.setText(TextData);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TEST().execute();
            }
        });
    }

    private class TEST extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Notification_closed.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://leo1997.000webhostapp.com/php/UpdateIsSave.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", id)
                        .appendQueryParameter("table", Data);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.dismiss();
                Toast toast = Toast.makeText(Notification_closed.this, "網路出現錯誤請檢查是否有網路連線", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("Record updated successfully")) {
                Toast toast = Toast.makeText(Notification_closed.this, "更新完成", Toast.LENGTH_LONG);
                ;
                toast.show();
                Intent intentService = new Intent(Notification_closed.this, NickyService.class);
                intentService.putExtra("back_data", id);
                startService(intentService);
                finish();
            } else {
                Toast toast = Toast.makeText(Notification_closed.this, "Exception", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//Return key lock
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:    //return key
                return true;   //since the break will exit, we have to deal with it ourselves. We will not return to the previous layer.
        }
        return super.onKeyDown(keyCode, event);
    }
}
