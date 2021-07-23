package com.example.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;

public class MainPageByAdmin extends AppCompatActivity {
    Button btnAdd, btnReset;
    ImageButton btnLogout, btnUserProfile;
    ListView listData;
    SearchView searchView;
    Spinner spinner;
    String[] id;
    String[] firstname;
    String[] lastname;
    String[] username;
    String[] password;
    String[] bedRoomNumber;
    String[] address;
    String[] tel;
    String[] email;
    String[] age;
    String[] departmentID;
    String[] precautions;
    String[] adminDetail;
    boolean Search = false; //Determine is using search
    boolean type = false; //Determine whether the information currently displayed is a doctor or a patient
    final String[] patientColumn = {"id", "firstname", "lastName", "roomnum", "address", "tel", "email", "age", "departmentID", "username", "password"};
    final String[] doctorColumn = {"id", "firstname", "lastName", "departmentID", "username", "password"};
    String SearchName_spinner = "";
    String SearchName = "";
    private Button btnListPatientDetail, btnListDoctorData;
    SharedPreferences userData;

    ListDepartment ListDepartment = new ListDepartment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_by_admin);
        btnAdd = (Button) findViewById(R.id.ButtonAdd);
        listData = (ListView) findViewById(R.id.listData);
        searchView = (SearchView) findViewById(R.id.searchview);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnListPatientDetail = (Button) findViewById(R.id.btnListPatientDetail);
        btnListDoctorData = (Button) findViewById(R.id.btnListDoctorData);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnUserProfile = (ImageButton) findViewById(R.id.ButtonUserProfile);
        Bundle bundle = getIntent().getExtras();
        adminDetail = bundle.getStringArray("adminDetail");
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData = getSharedPreferences("userData", MODE_PRIVATE);
                userData.edit().putString("username", "").commit();
                userData.edit().putString("password", "").commit();
                userData.edit().putBoolean("remember", false).commit();
                Intent intent = new Intent();
                intent.setClass(MainPageByAdmin.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageByAdmin.this, UserProfileByAdmin.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", adminDetail[1]);
                bundle.putString("username", adminDetail[2]);
                bundle.putString("password", adminDetail[3]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ListDepartment.getData();

        btnListDoctorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = false; //display patient
                new MainPageByAdmin.MainPageByAdminInBackground().execute();
                ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<>(MainPageByAdmin.this, android.R.layout.simple_spinner_dropdown_item, patientColumn);
                spinner.setAdapter(ArrayAdapter); //display patient information length
            }
        });
        btnListDoctorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //display doctor
                type = true;
                new MainPageByAdmin.MainPageByAdminInBackground().execute();
                ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<>(MainPageByAdmin.this, android.R.layout.simple_spinner_dropdown_item, doctorColumn);
                spinner.setAdapter(ArrayAdapter); //display doctor information length
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type) {
                    Intent intent = new Intent();
                    intent.setClass(MainPageByAdmin.this, AddPatientDetail.class);
                    intent.putExtra("ListDepartment", ListDepartment);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MainPageByAdmin.this, AddDoctorDetail.class);
                    startActivity(intent);
                }
            }
        });
        btnReset = (Button) findViewById(R.id.ButtonReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search = false;
                new MainPageByAdminInBackground().execute();
            }
        });
        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<>(MainPageByAdmin.this, android.R.layout.simple_spinner_dropdown_item, patientColumn);
        spinner.setAdapter(ArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SearchName_spinner = patientColumn[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        new MainPageByAdmin.MainPageByAdminInBackground().execute();
        listData.setOnItemClickListener(listener);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search = true;
                SearchName = query;
                new MainPageByAdmin.MainPageByAdminInBackground().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class MainPageByAdminInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainPageByAdmin.this);

        String jsonString1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        protected String doInBackground(String... countTo) {
            if (!Search) {
                // not searching
                try {
                    String uel = "";
                    if (!type) { //get patient data
                        uel = "https://leo1997.000webhostapp.com/php2/ListPatientDetail.php";
                    } else { //get doctor data
                        uel = "https://leo1997.000webhostapp.com/php2/ListDoctorDetail.php";
                    }
                    HttpURLConnection conn = null;
                    URL url = new URL(uel);
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
            } else {
                //searching
                HttpURLConnection conn;
                URL url = null;
                String uel = "";
                if (!type) {
                    uel = "https://leo1997.000webhostapp.com/php/ListPatientDetailBySearch.php"; //search patient
                } else {
                    uel = "https://leo1997.000webhostapp.com/php/ListDoctorDetailBySearch.php"; //search doctor
                }
                try {
                    url = new URL(uel);
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
                            .appendQueryParameter("type", SearchName_spinner)
                            .appendQueryParameter("value", SearchName);
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
            return jsonString1;
        }

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equals("false")) {
                Toast toast = Toast.makeText(MainPageByAdmin.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                Search = false;
                new MainPageByAdmin.MainPageByAdminInBackground().execute();
                return;
            }
            super.onPreExecute();
            if (!type) {
                //patient json
                try {
                    JSONArray dataArray = new JSONArray(result);
                    id = new String[dataArray.length()];
                    firstname = new String[dataArray.length()];
                    lastname = new String[dataArray.length()];
                    username = new String[dataArray.length()];
                    password = new String[dataArray.length()];
                    bedRoomNumber = new String[dataArray.length()];
                    address = new String[dataArray.length()];
                    tel = new String[dataArray.length()];
                    email = new String[dataArray.length()];
                    age = new String[dataArray.length()];
                    precautions = new String[dataArray.length()];
                    departmentID = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        id[i] = dataArray.getJSONObject(i).getString("id");
                        firstname[i] = dataArray.getJSONObject(i).getString("firstname");
                        lastname[i] = dataArray.getJSONObject(i).getString("lastname");
                        username[i] = dataArray.getJSONObject(i).getString("username");
                        password[i] = dataArray.getJSONObject(i).getString("password");
                        bedRoomNumber[i] = dataArray.getJSONObject(i).getString("roomnum");
                        address[i] = dataArray.getJSONObject(i).getString("address");
                        tel[i] = dataArray.getJSONObject(i).getString("tel");
                        email[i] = dataArray.getJSONObject(i).getString("email");
                        age[i] = dataArray.getJSONObject(i).getString("age");
                        precautions[i] = dataArray.getJSONObject(i).getString("precautions");
                        departmentID[i] = dataArray.getJSONObject(i).getString("departmentID");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(MainPageByAdmin.this, "JSONException", Toast.LENGTH_LONG);
                    toast.show();
                }
                List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                for (int index = 0; index < id.length; index++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id_name", "ID: " + id[index]);
                    map.put("name", "Name: " + firstname[index] + " " + lastname[index]);
                    map.put("bed_room_number", "Room: " + bedRoomNumber[index]);
                    data.add(map);
                }
                ListView listView = (ListView) findViewById(R.id.list);
                Adapter adapter = new Adapter(MainPageByAdmin.this, data);
                listView.setAdapter(adapter);
            } else {
                //doctor json
                try {
                    JSONArray dataArray = new JSONArray(result);
                    id = new String[dataArray.length()];
                    firstname = new String[dataArray.length()];
                    lastname = new String[dataArray.length()];
                    username = new String[dataArray.length()];
                    password = new String[dataArray.length()];
                    departmentID = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        id[i] = dataArray.getJSONObject(i).getString("id");
                        firstname[i] = dataArray.getJSONObject(i).getString("firstname");
                        lastname[i] = dataArray.getJSONObject(i).getString("lastname");
                        username[i] = dataArray.getJSONObject(i).getString("username");
                        password[i] = dataArray.getJSONObject(i).getString("password");
                        departmentID[i] = dataArray.getJSONObject(i).getString("departmentID");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(MainPageByAdmin.this, "JSONException", Toast.LENGTH_LONG);
                    toast.show();
                }
                List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                for (int index = 0; index < id.length; index++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id_name", "ID: " + id[index]);
                    map.put("name", "Name: " + firstname[index] + " " + lastname[index]);
                    map.put("bed_room_number", "departmentID: " + departmentID[index]);
                    data.add(map);
                }
                ListView listView = (ListView) findViewById(R.id.list);
                Adapter adapter = new Adapter(MainPageByAdmin.this, data);
                listView.setAdapter(adapter);
            }
        }
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
            if (!type) {
                String id1 = id[(int) arg3];
                String firstname1 = firstname[(int) arg3];
                String lastname1 = lastname[(int) arg3];
                String username1 = username[(int) arg3];
                String password1 = password[(int) arg3];
                String bedRoomNumber1 = bedRoomNumber[(int) arg3];
                String address1 = address[(int) arg3];
                String tel1 = tel[(int) arg3];
                String email1 = email[(int) arg3];
                String age1 = age[(int) arg3];
                String departmentID1 = departmentID[(int) arg3];
                String precautions1 = precautions[(int) arg3];

                Intent intent = new Intent();
                intent.setClass(MainPageByAdmin.this, ListPatientDetailByAdmin.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id1);
                bundle.putString("firstname", firstname1);
                bundle.putString("lastname", lastname1);
                bundle.putString("username", username1);
                bundle.putString("password", password1);
                bundle.putString("bedRoomNumber", bedRoomNumber1);
                bundle.putString("address", address1);
                bundle.putString("tel", tel1);
                bundle.putString("email", email1);
                bundle.putString("age", age1);
                bundle.putString("departmentID", departmentID1);
                bundle.putString("precautions", precautions1);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                String id1 = id[(int) arg3];
                String firstname1 = firstname[(int) arg3];
                String lastname1 = lastname[(int) arg3];
                String username1 = username[(int) arg3];
                String password1 = password[(int) arg3];
                String departmentID1 = departmentID[(int) arg3];

                Intent intent = new Intent();
                intent.setClass(MainPageByAdmin.this, ListDoctorDetailByAdmin.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id1);
                bundle.putString("firstname", firstname1);
                bundle.putString("lastname", lastname1);
                bundle.putString("username", username1);
                bundle.putString("password", password1);
                bundle.putString("departmentID", departmentID1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onResume() {// Update when returning to the page again
        super.onResume();
        new MainPageByAdminInBackground().execute();
    }
}
