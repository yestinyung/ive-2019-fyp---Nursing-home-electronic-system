package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

public class AddPatientDetail extends AppCompatActivity {
    EditText firstname, lastname, BedRoomNumber, Address, Tel, Email, Age, precautions, username, password;
    ImageButton btnAdd;
    Spinner department;
    String selectedDepartmentID = "null";
    String[] departmentName;
    String[] departmentID;
    int num = 0;
    boolean haveData = false; //Record whether the department's information is obtained

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient_detail);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        BedRoomNumber = (EditText) findViewById(R.id.bed_room_number);
        Address = (EditText) findViewById(R.id.address);
        Tel = (EditText) findViewById(R.id.tel);
        Email = (EditText) findViewById(R.id.email);
        Age = (EditText) findViewById(R.id.age);
        precautions = (EditText) findViewById(R.id.precautions);
        btnAdd = (ImageButton) findViewById(R.id.ButtonAdd);
        department = (Spinner) findViewById(R.id.department);
        Intent intent = getIntent();
        ListDepartment Listdepartment = (ListDepartment) intent.getSerializableExtra("ListDepartment");
        haveData = Listdepartment.haveData(); // Access to get information
        if (!haveData) {
            Toast toast = Toast.makeText(AddPatientDetail.this, "Error", Toast.LENGTH_LONG);
            toast.show();
        } else {
            departmentID = Listdepartment.getDepartmentID(); // Access to get deapartment ID
            departmentName = Listdepartment.getDepartmentName(); // Access to get deapartment name
            ArrayAdapter<String> lunchList = new ArrayAdapter<>(AddPatientDetail.this, android.R.layout.simple_spinner_dropdown_item, departmentName);
            department.setAdapter(lunchList); //stored data
            department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDepartmentID = departmentID[position]; //stored dearpartment ID
                    num = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOneDialog();
            }
        });
    }

    private class AddPatientDetailInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddPatientDetail.this);
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
            final String Firstname1 = firstname.getText().toString();
            final String Lastname1 = lastname.getText().toString();
            final String username1 = username.getText().toString();
            final String password1 = password.getText().toString();
            final String BedRoomNumber1 = BedRoomNumber.getText().toString();
            final String Address1 = Address.getText().toString();
            final String Tel1 = Tel.getText().toString();
            final String Email1 = Email.getText().toString();
            final String Age1 = Age.getText().toString();
            final String Precautions1 = precautions.getText().toString();
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/AddPatientDetail.php");
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
                        .appendQueryParameter("firstname", Firstname1)
                        .appendQueryParameter("lastname", Lastname1)
                        .appendQueryParameter("username", username1)
                        .appendQueryParameter("password", password1)
                        .appendQueryParameter("BedRoomNumber", BedRoomNumber1)
                        .appendQueryParameter("Address", Address1)
                        .appendQueryParameter("Tel", Tel1)
                        .appendQueryParameter("Email", Email1)
                        .appendQueryParameter("Age", Age1)
                        .appendQueryParameter("departmentID", selectedDepartmentID)
                        .appendQueryParameter("precautions", Precautions1);
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
                Toast toast = Toast.makeText(AddPatientDetail.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("New records created successfully")) {
                Toast toast = Toast.makeText(AddPatientDetail.this, "New records created successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(AddPatientDetail.this, "Exception", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
    }

    private void showOneDialog() {
        final android.app.AlertDialog build = new android.app.AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.splash_dialog, null);
        build.setView(view, 0, 0, 0, 0);
        build.show();
        int width = getWindowManager().getDefaultDisplay().getWidth();
        WindowManager.LayoutParams params = build.getWindow().getAttributes();
        params.width = width - (width / 6);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        build.getWindow().setAttributes(params);
        Button leftButton = (Button) view.findViewById(R.id.splash_dialog_left);
        Button rightButton = (Button) view.findViewById(R.id.splash_dialog_right);
        TextView warnMessage = (TextView) view.findViewById(R.id.warnmessage);
        TextView warnMessage1 = (TextView) view.findViewById(R.id.TextView);
        warnMessage1.setText("Update?");
        final String Firstname1 = firstname.getText().toString();
        final String LastName1 = lastname.getText().toString();
        final String username1 = username.getText().toString();
        final String password1 = password.getText().toString();
        final String BedRoomNumber1 = BedRoomNumber.getText().toString();
        final String Address1 = Address.getText().toString();
        final String Tel1 = Tel.getText().toString();
        final String Email1 = Email.getText().toString();
        final String Age1 = Age.getText().toString();
        final String Precautions1 = precautions.getText().toString();
        warnMessage.setText("Firstname: " + Firstname1 + "\n" + "LastName: " + LastName1 + "\n" + "username: " + username1 + "\n" + "password: " + password1 + "\n" + "BedRoomNumber: " + BedRoomNumber1 + "\n" + "Address: " + Address1 + "\n" + "Tel: " + Tel1 + "\n" + "Email: " + Email1 + "\n" + "Age: " + Age1 + "\n" + "Department: " + departmentName[num] + "\n" + "Precautions: " + Precautions1);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddPatientDetailInBackground().execute();
                build.dismiss();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build.dismiss();
            }
        });
    }
}

