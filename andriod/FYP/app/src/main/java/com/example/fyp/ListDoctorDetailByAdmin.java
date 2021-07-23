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

public class ListDoctorDetailByAdmin extends AppCompatActivity {
    TextView txtID;
    TextView txtFirstname;
    TextView txtLastname;
    TextView txtUsername;
    TextView txtPassword;
    TextView txtDepartment;
    String id, firstname, lastname, username, password, departmentID;
    Button ButtonUpdate, ButtonDisable;
    String[] departmentName, AllDepartmentID;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_doctor_detail_by_admin);
        txtID = (TextView) findViewById(R.id.id);
        txtFirstname = (TextView) findViewById(R.id.firstname);
        txtLastname = (TextView) findViewById(R.id.lastname);
        txtUsername = (TextView) findViewById(R.id.username);
        txtPassword = (TextView) findViewById(R.id.password);
        txtDepartment = (TextView) findViewById(R.id.department);
        ButtonUpdate = (Button) findViewById(R.id.ButtonUpdate);
        ButtonDisable = (Button) findViewById(R.id.ButtonDisable);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastData");
        username = bundle.getString("username");
        password = bundle.getString("password");
        departmentID = bundle.getString("departmentID");
        Intent intent = getIntent();
        ListDepartment Listdepartment = (ListDepartment) intent.getSerializableExtra("Name");
        departmentName = Listdepartment.getDepartmentName(); // Access to get deapartment name
        AllDepartmentID = Listdepartment.getDepartmentID();
        txtID.setText("id:        "+id);
        txtFirstname.setText("firstname: "+firstname);
        txtLastname.setText("lastname:  "+lastname);
        txtUsername.setText("username:  "+username);
        txtPassword.setText("password:  "+password);
        for (i = 0 ; i <= AllDepartmentID.length; i++) {
            if (AllDepartmentID[i].equals(departmentID)){
                break;
            }
        }
        txtDepartment.setText("department:"+departmentName[i]);
        ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListDoctorDetailByAdmin.this, UpdateDoctorDetailByAdmin.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("firstname", firstname);
                bundle.putString("lastname", lastname);
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("departmentID", departmentID);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        ButtonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOneDialog();
            }
        });
    }

    private class ListDoctorDetailByAdminInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ListDoctorDetailByAdmin.this);
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
                        .appendQueryParameter("Id", id)
                        .appendQueryParameter("Firstname", firstname)
                        .appendQueryParameter("LastName", lastname)
                        .appendQueryParameter("username", "")
                        .appendQueryParameter("password", "")
                        .appendQueryParameter("departmentID", departmentID);
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
                Toast toast = Toast.makeText(ListDoctorDetailByAdmin.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("Record updated successfully")) {
                Toast toast = Toast.makeText(ListDoctorDetailByAdmin.this, "Record updated successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(ListDoctorDetailByAdmin.this, "Exception", Toast.LENGTH_LONG);
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
        TextView warnMessage = (TextView) view.findViewById(R.id.TextView);
        warnMessage.setText("Update?");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListDoctorDetailByAdminInBackground().execute();
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
