package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
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


public class ListPatientDetailByDoctor extends AppCompatActivity {
    TextView txtID;
    TextView txtFirstname;
    TextView txtLastname;
    TextView txtBedRoomNumber;
    TextView txtAddress;
    TextView txtTel;
    TextView txtEmail;
    TextView txtAge;
    TextView txtDepartment;
    TextView txtPrecautions;
    String id, firstname, lastname, bedRoomNumber, address, tel, email, age, departmentID, precautions, DoctorID;
    ImageButton btnUpdate, medicalrecordUpdate, btnStatus;
    String[] id2;
    String[] medicalRecord;
    String[] date;
    String[] patientID;
    String[] AllDoctorID;
    String[] departmentName, AllDepartmentID;
    int i = 0;
    ListDepartment Listdepartment = new ListDepartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_patient_detail_by_doctor);
        txtID = (TextView) findViewById(R.id.id);
        txtFirstname = (TextView) findViewById(R.id.firstname);
        txtLastname = (TextView) findViewById(R.id.lastname);
        txtBedRoomNumber = (TextView) findViewById(R.id.bedRoomNumber);
        txtAddress = (TextView) findViewById(R.id.address);
        txtTel = (TextView) findViewById(R.id.tel);
        txtEmail = (TextView) findViewById(R.id.email);
        txtAge = (TextView) findViewById(R.id.age);
        txtDepartment = (TextView) findViewById(R.id.department);
        txtPrecautions = (TextView) findViewById(R.id.precautions);
        btnUpdate = (ImageButton) findViewById(R.id.ButtonUpdate);
        medicalrecordUpdate = (ImageButton) findViewById(R.id.medicalrecordUpdate);
        btnStatus = (ImageButton) findViewById(R.id.Buttonstatus);
        Bundle bundle = getIntent().getExtras();
        DoctorID = bundle.getString("DoctorID");
        id = bundle.getString("id");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");
        bedRoomNumber = bundle.getString("bedRoomNumber");
        address = bundle.getString("address");
        tel = bundle.getString("tel");
        email = bundle.getString("email");
        age = bundle.getString("age");
        departmentID = bundle.getString("departmentID");
        precautions = bundle.getString("precautions");
        Listdepartment.getData();
        departmentName = Listdepartment.getDepartmentName(); // Access to get deapartment name
        AllDepartmentID = Listdepartment.getDepartmentID();
        txtID.setText("id: " + id);
        txtFirstname.setText("firstname: " + firstname);
        txtLastname.setText("lastname: " + lastname);
        txtBedRoomNumber.setText("room number: " + bedRoomNumber);
        txtAddress.setText("address: " + address);
        txtTel.setText("tel: " + tel);
        txtEmail.setText("email" + email);
        txtAge.setText("age: " + age);
        for (i = 0; i <= AllDepartmentID.length; i++) {
            if (AllDepartmentID[i].equals(departmentID)) {
                break;
            }
        }
        txtDepartment.setText("department:" + departmentName[i]);
        txtPrecautions.setText("precautions: "+precautions);
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListPatientDetailByDoctor.this, ListHeartbeatAndBodyTemperature.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        medicalrecordUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListPatientDetailByDoctor.this, AddMedicalrecord.class);
                Bundle bundle = new Bundle();
                bundle.putString("DoctorID", DoctorID);
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListPatientDetailByDoctor.this, UpdatePatientDetailByDoctor.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("firstname", firstname);
                bundle.putString("lastname", lastname);
                bundle.putString("bed_room_number", bedRoomNumber);
                bundle.putString("address", address);
                bundle.putString("tel", tel);
                bundle.putString("email", email);
                bundle.putString("age", age);
                bundle.putString("departmentID", departmentID);
                bundle.putString("precautions", precautions);
                intent.putExtra("Listdepartment", Listdepartment);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        new TEST().execute();
    }

    private class TEST extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ListPatientDetailByDoctor.this);

        String jsonString1;

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
                url = new URL("https://leo1997.000webhostapp.com/php2/ListPatientDetailByID.php");
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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("id", id);
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
                Toast toast = Toast.makeText(ListPatientDetailByDoctor.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            super.onPreExecute();
            try {
                JSONArray dataArray = new JSONArray(result);
                id2 = new String[dataArray.length()];
                medicalRecord = new String[dataArray.length()];
                date = new String[dataArray.length()];
                patientID = new String[dataArray.length()];
                AllDoctorID = new String[dataArray.length()];
                for (int i = 0; i < dataArray.length(); i++) {
                    id2[i] = dataArray.getJSONObject(i).getString("id");
                    medicalRecord[i] = dataArray.getJSONObject(i).getString("medicalRecord");
                    date[i] = dataArray.getJSONObject(i).getString("date");
                    patientID[i] = dataArray.getJSONObject(i).getString("patientID");
                    AllDoctorID[i] = dataArray.getJSONObject(i).getString("doctorID");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(ListPatientDetailByDoctor.this, "Error", Toast.LENGTH_LONG);
                toast.show();
            }
            for (int i = 0; i < id2.length; i++) { //display medical record
                txtPrecautions.append("\n");
                txtPrecautions.append(medicalRecord[i] + "\n");
                txtPrecautions.append("\n");
                txtPrecautions.append(date[i] + "\n");
            }
        }
    }
}
