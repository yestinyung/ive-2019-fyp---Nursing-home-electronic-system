package com.example.fyp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class UserProfileByPatient extends AppCompatActivity {
    String id1, firstname1, lastname1, bedRoomNumber1, address1, tel1, email1, age1, departmentID1, precautions1, username1, password1;
    ImageButton btnUpdate;
    EditText EditFirstname, EditLastname, EditAddress, EditTel, EditEmail, EditUsername, EditPassword, EditConfirmPassword;
    TextView txtPatientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_by_patient);
        EditFirstname = (EditText) findViewById(R.id.firstname);
        EditLastname = (EditText) findViewById(R.id.lastname);
        EditUsername = (EditText) findViewById(R.id.username);
        EditPassword = (EditText) findViewById(R.id.password);
        EditConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        EditAddress = (EditText) findViewById(R.id.address);
        EditTel = (EditText) findViewById(R.id.tel);
        EditEmail = (EditText) findViewById(R.id.email);
        txtPatientID = (TextView) findViewById(R.id.TextView);
        btnUpdate = (ImageButton) findViewById(R.id.ButtonUpdate);
        Bundle bundle = getIntent().getExtras();
        id1 = bundle.getString("id");
        firstname1 = bundle.getString("firstname");
        lastname1 = bundle.getString("lastname");
        username1 = bundle.getString("username");
        password1 = bundle.getString("password");
        bedRoomNumber1 = bundle.getString("bedRoomNumber");
        address1 = bundle.getString("address");
        tel1 = bundle.getString("tel");
        email1 = bundle.getString("email");
        age1 = bundle.getString("age");
        departmentID1 = bundle.getString("departmentID");
        precautions1 = bundle.getString("precautions");
        txtPatientID.setText("ID: " + id1);
        EditFirstname.setText(firstname1);
        EditLastname.setText(lastname1);
        EditUsername.setText(username1);
        EditPassword.setText(password1);
        EditAddress.setText(address1);
        EditTel.setText(tel1);
        EditEmail.setText(email1);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(EditPassword.getText().toString().equals(EditConfirmPassword.getText().toString()))) {
                    Toast toast = Toast.makeText(UserProfileByPatient.this, "wrong password", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    showOneDialog();
                }
            }
        });
    }

    private class UserProfileByPatientInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(UserProfileByPatient.this);
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
            final String id2 = id1;
            final String firstname2 = EditFirstname.getText().toString();
            final String lastname2 = EditLastname.getText().toString();
            final String username2 = EditUsername.getText().toString();
            final String password2 = EditPassword.getText().toString();
            final String bedRoomNumber2 = bedRoomNumber1;
            final String address2 = EditAddress.getText().toString();
            final String tel2 = EditTel.getText().toString();
            final String email2 = EditEmail.getText().toString();
            final String age2 = age1;
            final String departmentID2 = departmentID1;
            final String precautions2 = precautions1;
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/UpdatePatientDetail.php");
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
                        .appendQueryParameter("id", id2)
                        .appendQueryParameter("firstname", firstname2)
                        .appendQueryParameter("lastname", lastname2)
                        .appendQueryParameter("username", username2)
                        .appendQueryParameter("password", password2)
                        .appendQueryParameter("BedRoomNumber", bedRoomNumber2)
                        .appendQueryParameter("Address", address2)
                        .appendQueryParameter("Tel", tel2)
                        .appendQueryParameter("Email", email2)
                        .appendQueryParameter("Age", age2)
                        .appendQueryParameter("departmentID", departmentID2)
                        .appendQueryParameter("precautions", precautions2);
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
                Toast toast = Toast.makeText(UserProfileByPatient.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("Record updated successfully")) {
                Toast toast = Toast.makeText(UserProfileByPatient.this, "Record updated successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            } else {
                Toast toast = Toast.makeText(UserProfileByPatient.this, "Exception", Toast.LENGTH_LONG);
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
        final String firstname2 = EditFirstname.getText().toString();
        final String lastname2 = EditLastname.getText().toString();
        final String username2 = EditUsername.getText().toString();
        final String password2 = EditPassword.getText().toString();
        final String address2 = EditAddress.getText().toString();
        final String tel2 = EditTel.getText().toString();
        final String email2 = EditEmail.getText().toString();
        warnMessage.setText("ID: " + id1 + "\n" + "Firstname: " + firstname2 + "\n" + "Lastname: "
                + lastname2 + "\n" + "Username: " + username2 + "\n" + "Password: " + password2
                + "\n" + "Address: " + address2 + "\n" + "Tel: " + tel2 + "\n" + "Email: " + email2);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserProfileByPatientInBackground().execute();
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



