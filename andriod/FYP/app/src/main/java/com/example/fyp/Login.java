package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Login extends AppCompatActivity {
    ImageButton btnLogin;
    TextView mytest;
    EditText password, username;
    SharedPreferences userData;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnLogin = (ImageButton) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        remember = (CheckBox) findViewById(R.id.remember);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        username.setText(getSharedPreferences("userData", MODE_PRIVATE).getString("username", ""));
        password.setText(getSharedPreferences("userData", MODE_PRIVATE).getString("password", ""));
        remember.setChecked(getSharedPreferences("userData", MODE_PRIVATE).getBoolean("remember", false));
        if (getSharedPreferences("userData", MODE_PRIVATE).getBoolean("remember", false)) {
            new LoginInBackground().execute();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username1 = username.getText().toString();
                final String password1 = password.getText().toString();
                if (username1.equals("") || password1.equals("")) {
                    Toast toast = Toast.makeText(Login.this, "username or password can not null", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    if (remember.isChecked()) {
                        userData.edit().putString("username", username.getText().toString()).commit();
                        userData.edit().putString("password", password.getText().toString()).commit();
                        userData.edit().putBoolean("remember", true).commit();
                    } else {
                        userData.edit().putString("username", "").commit();
                        userData.edit().putString("password", "").commit();
                        userData.edit().putBoolean("remember", false).commit();
                    }
                    new LoginInBackground().execute();
                }
            }
        });
    }

    private class LoginInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
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
            final String username1 = username.getText().toString();
            final String password1 = password.getText().toString();
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/Login.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "MalformedURLException";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username1)
                        .appendQueryParameter("password", password1);
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
                return "IOException";
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
                return "IOException";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.dismiss();
                Toast toast = Toast.makeText(Login.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("false")) {
                Toast toast = Toast.makeText(Login.this, "wrong password", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else {
                String[] array;
                try {
                    array = result.split(","); //The data format of the login maneuver will be separated by ","
                    for (int i = 0; i < array.length; i++) {
                        System.out.println("array[" + i + "] = " + array[i]); //testing
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(Login.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if (4 == array.length) { //if return length is 4 = admin
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent();
                    bundle.putStringArray("adminDetail",array);
                    intent.setClass(Login.this, MainPageByAdmin.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else if (8 == array.length) {//if return length is 8 = doctor
                    Intent intentService = new Intent(Login.this, NickyService.class);
                    intentService.putExtra("input", array[2]);
                    startService(intentService);
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("doctorDetail", array);
                    Intent intent = new Intent();
                    intent.setClass(Login.this, MainPageByDoctor.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else if (array.length > 8) {// if return length > 8 = patient
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("patientDetail", array);
                    Intent intent = new Intent();
                    intent.setClass(Login.this, MainPageByPatient.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}