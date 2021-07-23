package com.example.fyp;

/*

*/

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMedicalrecord extends AppCompatActivity {
    String patientID, DoctorID;
    TextView txtpatientID;
    EditText medicalrecord;
    ImageButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medical_record);
        txtpatientID = (TextView) findViewById(R.id.patientID);
        medicalrecord = (EditText) findViewById(R.id.medicalrecord);
        btnAdd = (ImageButton) findViewById(R.id.ButtonAdd);
        Bundle bundle = getIntent().getExtras();
        DoctorID = bundle.getString("DoctorID"); //get the doctor ID from last page
        patientID = bundle.getString("patientID"); // get the patient ID from last page
        txtpatientID.setText("patientID:"+patientID);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMedicalrecordInBackground().execute();
            }
        });
    }

    private class AddMedicalrecordInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddMedicalrecord.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        protected String doInBackground(String... countTo) {
            Time mTime = new Time("GMT+8");
            mTime.setToNow();
            final String medicalrecord1 = medicalrecord.getText().toString();
            final String recordDate = Integer.toString(mTime.monthDay) + "-" + Integer.toString(mTime.month + 1) // system dected month 0-11, so will need to set month +1
                    + "-" + Integer.toString(mTime.year) + " " + Integer.toString(mTime.hour) + ":" + Integer.toString(mTime.minute)
                    + ":" + Integer.toString(mTime.second); //Access system time
            HttpURLConnection conn;
            URL url = null;
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/AddMedicalrecord.php");
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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", patientID)
                        .appendQueryParameter("medicalrecord", medicalrecord1)
                        .appendQueryParameter("recordDate", recordDate)
                        .appendQueryParameter("DoctorID", DoctorID);
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
            pdLoading.dismiss(); //Dialog disappears
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.dismiss();
                Toast toast = Toast.makeText(AddMedicalrecord.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else {
                finish();
            }
        }
    }
}

