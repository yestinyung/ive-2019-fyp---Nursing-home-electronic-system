package com.example.fyp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class UserProfileByDoctor extends AppCompatActivity {

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
        setContentView(R.layout.user_profile_by_doctor);
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
        EditPassword.setText(password);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOneDialog();
            }
        });
    }

    private class UserProfileByDoctorInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(UserProfileByDoctor.this);
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
                Toast toast = Toast.makeText(UserProfileByDoctor.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("Record updated successfully")) {
                Toast toast = Toast.makeText(UserProfileByDoctor.this, "Record updated successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(UserProfileByDoctor.this, "Exception", Toast.LENGTH_LONG);
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
        warnMessage.setText("ID: " + id + "\n" + "Firstname: " + firstname1 + "\n" + "Lastname: " + lastname1 + "\n" + "Username: " + username1 + "\n" + "Password: " + password1);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserProfileByDoctor.UserProfileByDoctorInBackground().execute();
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


