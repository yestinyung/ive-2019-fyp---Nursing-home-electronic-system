package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.ArrayList;

public class MainPageByPatient extends AppCompatActivity {
    String[] patientDetail;
    String[] id;
    String[] medicalRecord;
    String[] date;
    String[] patientID;
    String[] doctorID;
    TextView txtwelcome;
    TextView txtBedRoomNumber;
    TextView txtDepartment;
    TextView txtPrecautions;

    private ImageButton btnUserProfile, btnBedControler, btnStatus;
    SharedPreferences userData;
    String[] departmentName, AllDepartmentID;
    int i = 0;
    ListDepartment Listdepartment = new ListDepartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_by_patient);
        Bundle bundle = getIntent().getExtras();
        txtwelcome = (TextView) findViewById(R.id.welcome);
        txtBedRoomNumber = (TextView) findViewById(R.id.bedRoomNumber);
        txtDepartment = (TextView) findViewById(R.id.department);
        txtPrecautions = (TextView) findViewById(R.id.precautions);
        Listdepartment.getData();
        departmentName = Listdepartment.getDepartmentName(); // Access to get deapartment name
        AllDepartmentID = Listdepartment.getDepartmentID();
        btnUserProfile = (ImageButton) findViewById(R.id.ButtonUserProfile);
        btnBedControler = (ImageButton) findViewById(R.id.ButtonBedControler);
        btnStatus = (ImageButton) findViewById(R.id.ButtonStatus);
        ImageButton btnLogout = (ImageButton) findViewById(R.id.ButtonLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData = getSharedPreferences("userData", MODE_PRIVATE);
                userData.edit().putString("username", "").commit();
                userData.edit().putString("password", "").commit();
                userData.edit().putBoolean("remember", false).commit();
                Intent intent = new Intent();
                intent.setClass(MainPageByPatient.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        patientDetail = bundle.getStringArray("patientDetail");
        txtwelcome.setText("HI " + patientDetail[2] + " " + patientDetail[3]);
        txtBedRoomNumber.setText("room number: " + patientDetail[11]);
        for (i = 0; i <= AllDepartmentID.length; i++) {
            if (AllDepartmentID[i].equals(patientDetail[10])) {
                break;
            }
        }
        txtDepartment.setText("department: " + departmentName[i]);
        txtPrecautions.setText("precautions: " + patientDetail[9]);
        btnBedControler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageByPatient.this, BedControlerByPatient.class);
                startActivity(intent);
            }
        });
        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageByPatient.this, UserProfileByPatient.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", patientDetail[1]);
                bundle.putString("firstname", patientDetail[2]);
                bundle.putString("lastname", patientDetail[3]);
                bundle.putString("username", patientDetail[4]);
                bundle.putString("password", patientDetail[5]);
                bundle.putString("bedRoomNumber", patientDetail[12]);
                bundle.putString("address", patientDetail[6]);
                bundle.putString("tel", patientDetail[7]);
                bundle.putString("email", patientDetail[8]);
                bundle.putString("age", patientDetail[9]);
                bundle.putString("departmentID", patientDetail[11]);
                bundle.putString("precautions", patientDetail[10]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageByPatient.this, ListHeartbeatAndBodyTemperature.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", patientDetail[1]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        new MainPageByPatientInBackground().execute();

    }

    private class MainPageByPatientInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainPageByPatient.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        protected String doInBackground(String... countTo) {
            HttpURLConnection conn;
            URL url = null;
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/ListMedicalrecordByPatientID.php");
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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", patientDetail[1]);
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

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equals("false")) {
                Toast toast = Toast.makeText(MainPageByPatient.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            super.onPreExecute();
            try {
                JSONArray dataArray = new JSONArray(result);
                id = new String[dataArray.length()];
                medicalRecord = new String[dataArray.length()];
                date = new String[dataArray.length()];
                patientID = new String[dataArray.length()];
                doctorID = new String[dataArray.length()];
                for (int i = 0; i < dataArray.length(); i++) {
                    id[i] = dataArray.getJSONObject(i).getString("id");
                    medicalRecord[i] = dataArray.getJSONObject(i).getString("medicalRecord");
                    date[i] = dataArray.getJSONObject(i).getString("date");
                    patientID[i] = dataArray.getJSONObject(i).getString("patientID");
                    doctorID[i] = dataArray.getJSONObject(i).getString("doctorID");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(MainPageByPatient.this, "JSONException", Toast.LENGTH_LONG);
                toast.show();
            }
            for (int i = 0; i < id.length; i++) { //display medical record
                txtPrecautions.append("\n");
                txtPrecautions.append(medicalRecord[i] + "\n");
                txtPrecautions.append("\n");
                txtPrecautions.append(date[i] + "\n");
            }
        }
    }
}
