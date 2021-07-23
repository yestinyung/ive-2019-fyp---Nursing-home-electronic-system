package com.example.fyp;

import android.app.ProgressDialog;
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

public class UpdateDoctorDetailByAdmin extends AppCompatActivity {

    String id, firstname, lastname, departmentID, username, password;
    ImageButton btnUpdate;
    EditText EditFirstname, EditLastname, EditUsername, EditPassword;
    TextView txtDoctorID;
    Spinner department;
    final String[] ListDepartment = {};
    String selectedDepartmentID = "";
    String[] departmentID2;
    String[] departmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_doctor_detail_by_admin);
        EditFirstname = (EditText) findViewById(R.id.firstname);
        EditLastname = (EditText) findViewById(R.id.lastname);
        EditUsername = (EditText) findViewById(R.id.username);
        EditPassword = (EditText) findViewById(R.id.password);
        btnUpdate = (ImageButton) findViewById(R.id.ButtonUpdate);
        txtDoctorID = (TextView) findViewById(R.id.doctorID);
        department = (Spinner) findViewById(R.id.department);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");
        username = bundle.getString("username");
        password = bundle.getString("password");
        departmentID = bundle.getString("departmentID");
        txtDoctorID.setText("ID: " + id);
        EditFirstname.setText(firstname);
        EditLastname.setText(lastname);
        EditUsername.setText(username);
        EditPassword.setText(username);
        new UpdateDoctorDetailByAdminInBackground2().execute();
        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<>(UpdateDoctorDetailByAdmin.this, android.R.layout.simple_spinner_dropdown_item, ListDepartment);
        department.setAdapter(ArrayAdapter);
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartmentID = departmentID2[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOneDialog();
            }
        });
    }

    private class UpdateDoctorDetailByAdminInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(UpdateDoctorDetailByAdmin.this);
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
            final String id1 = id;
            final String firstname1 = EditFirstname.getText().toString();
            final String lastname1 = EditLastname.getText().toString();
            final String username1 = EditUsername.getText().toString();
            final String password1 = EditPassword.getText().toString();
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/UpdateDoctorDetail.php");
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
                        .appendQueryParameter("id", id1)
                        .appendQueryParameter("firstname", firstname1)
                        .appendQueryParameter("lastname", lastname1)
                        .appendQueryParameter("username", username1)
                        .appendQueryParameter("password", password1)
                        .appendQueryParameter("departmentID", selectedDepartmentID);
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
                Toast toast = Toast.makeText(UpdateDoctorDetailByAdmin.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("Record updated successfully")) {
                Toast toast = Toast.makeText(UpdateDoctorDetailByAdmin.this, "Record updated successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(UpdateDoctorDetailByAdmin.this, "Exception", Toast.LENGTH_LONG);
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
        final String firstname1 = EditFirstname.getText().toString();
        final String lastname1 = EditLastname.getText().toString();
        final String username1 = EditUsername.getText().toString();
        final String password1 = EditPassword.getText().toString();
        warnMessage.setText("ID: " + id + "\n" + "Firstname: " + firstname1 + "\n" + "Lastname: " + lastname1 + "\n" + "username: " + username1 + "\n" + "password: " + password1 + "\n" + "\tdepartmentID: " + selectedDepartmentID);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateDoctorDetailByAdminInBackground().execute();
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

    private class UpdateDoctorDetailByAdminInBackground2 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(UpdateDoctorDetailByAdmin.this);
        HttpURLConnection conn;
        URL url = null;
        String jsonString1;

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
                HttpURLConnection conn = null;
                URL url = new URL("https://leo1997.000webhostapp.com/php/department.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                jsonString1 = reader.readLine();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
            return jsonString1;
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.dismiss();
                Toast toast = Toast.makeText(UpdateDoctorDetailByAdmin.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else {
                try {
                    JSONArray JSON = new JSONArray(result);
                    departmentID2 = new String[JSON.length()];
                    departmentName = new String[JSON.length()];
                    for (int i = 0; i < JSON.length(); i++) {
                        departmentID2[i] = JSON.getJSONObject(i).getString("id");
                        departmentName[i] = JSON.getJSONObject(i).getString("departmentName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(UpdateDoctorDetailByAdmin.this, "JSONException", Toast.LENGTH_LONG);
                    toast.show();
                }
                ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<>(UpdateDoctorDetailByAdmin.this, android.R.layout.simple_spinner_dropdown_item, departmentName);
                department.setAdapter(ArrayAdapter);
                department.setSelection(Integer.valueOf(departmentID) - 1);
            }
        }
    }
}


