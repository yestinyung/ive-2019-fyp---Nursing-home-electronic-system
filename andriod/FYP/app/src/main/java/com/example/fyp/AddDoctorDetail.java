package com.example.fyp;

/*
 create doctor account
*/

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

public class AddDoctorDetail extends AppCompatActivity {
    EditText firstname, lastname, username, password;
    ImageButton btnAdd;
    Spinner department;
    final String[] lunch = {};
    String selectedDepartmentID = ""; //Store the department ID selected by the user
    String[] departmentID;
    String[] departmentName;
    int num = 0; // Store the position selected by the user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor_detail);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        department = (Spinner) findViewById(R.id.department);
        btnAdd = (ImageButton) findViewById(R.id.ButtonAdd);
        new AddDoctorDetailInBackground_spinner().execute();
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(AddDoctorDetail.this, android.R.layout.simple_spinner_dropdown_item, lunch);
        department.setAdapter(lunchList);
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartmentID = departmentID[position]; //stored the select department ID
                num = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOneDialog();
            }
        });
    }

    private class AddDoctorDetailInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddDoctorDetail.this);
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
            final String LastName1 = lastname.getText().toString();
            final String username1 = username.getText().toString();
            final String password1 = password.getText().toString();
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/AddDoctorDetail.php");
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
                        .appendQueryParameter("lastname", LastName1)
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
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) { //system error
                pdLoading.dismiss();
                Toast toast = Toast.makeText(AddDoctorDetail.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (result.equalsIgnoreCase("New records created successfully")) {// php returen success
                Toast toast = Toast.makeText(AddDoctorDetail.this, "New records created successfully", Toast.LENGTH_LONG);
                toast.show();
                finish(); // end this page
            } else {
                Toast toast = Toast.makeText(AddDoctorDetail.this, "Exception", Toast.LENGTH_LONG); //php reutrn error
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
        warnMessage.setText("Firstname: " + Firstname1 + "\n" + "LastName: " + LastName1 + "\n" + "username: " + username1 + "\n" + "password: " + password1 + "\n" + "department: " + departmentName[num]);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddDoctorDetailInBackground().execute();
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

    private class AddDoctorDetailInBackground_spinner extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddDoctorDetail.this);
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
            pdLoading.dismiss(); //Dialog disappears
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast toast = Toast.makeText(AddDoctorDetail.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            } else {
                try {
                    JSONArray dataArray = new JSONArray(result);
                    departmentID = new String[dataArray.length()];
                    departmentName = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        departmentID[i] = dataArray.getJSONObject(i).getString("id");
                        departmentName[i] = dataArray.getJSONObject(i).getString("departmentName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(AddDoctorDetail.this, "JSONException", Toast.LENGTH_LONG);
                    toast.show();
                }
                ArrayAdapter<String> lunchList = new ArrayAdapter<>(AddDoctorDetail.this, android.R.layout.simple_spinner_dropdown_item, departmentName); //do the select bar
                department.setAdapter(lunchList);// set select list
            }
        }
    }
}

