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

public class MainPageByDoctor extends AppCompatActivity {
    Button btnAdd;
    ListView listview, listRoom;
    SearchView searchView;
    Spinner spinner;
    String[] doctorDetail;
    String[] id;
    String[] firstname;
    String[] lastname;
    String[] bedRoomNumber;
    String[] address;
    String[] tel;
    String[] email;
    String[] age;
    String[] departmentID;
    String[] precautions;
    String[] roomnum;
    boolean Search = false; //is doing searching
    boolean have_Data = false;
    final String[] patientColumn = {"id", "firstname", "lastName", "roomnum", "address", "tel", "email", "age", "Precautions", "departmentID"}; //searching bar list
    String SearchType = "";
    String SearchValue = "";
    String SearchDepartmentID;
    String DoctorID;
    private ImageButton btnListPatientDetail, btnListRoomDetail, btnReset, btnUserProfile, btnLogout;
    boolean displayRoom = false;
    SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_by_doctor);
        btnAdd = (Button) findViewById(R.id.ButtonAdd);
        listview = (ListView) findViewById(R.id.listData);
        listRoom = (ListView) findViewById(R.id.listRoomDetail);
        searchView = (SearchView) findViewById(R.id.searchview);
        spinner = (Spinner) findViewById(R.id.spinner);
        Bundle bundle = getIntent().getExtras();
        doctorDetail = bundle.getStringArray("doctorDetail");
        SearchDepartmentID = doctorDetail[7];
        DoctorID = doctorDetail[2];
        btnListPatientDetail = (ImageButton) findViewById(R.id.btnListPatientDetail);
        btnListRoomDetail = (ImageButton) findViewById(R.id.btnListRoomDetail);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnUserProfile = (ImageButton) findViewById(R.id.ButtonUserProfile);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userData = getSharedPreferences("userData", MODE_PRIVATE);
                userData.edit().putString("username", "").commit();
                userData.edit().putString("password", "").commit();
                userData.edit().putBoolean("remember", false).commit();
                Intent intent = new Intent();
                intent.setClass(MainPageByDoctor.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainPageByDoctor.this, UserProfileByDoctor.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", doctorDetail[3]);
                bundle.putString("firstname", doctorDetail[4]);
                bundle.putString("lastname", doctorDetail[5]);
                bundle.putString("username", doctorDetail[6]);
                bundle.putString("password", doctorDetail[7]);
                bundle.putString("departmentID", doctorDetail[8]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnListPatientDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                listRoom.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                displayRoom = false;
                new MainPageByDoctorInBackground().execute();
                ArrayAdapter<String> lunchList = new ArrayAdapter<>(MainPageByDoctor.this, android.R.layout.simple_spinner_dropdown_item, patientColumn); //display spinner data
                spinner.setAdapter(lunchList);
            }
        });
        btnListRoomDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display room data
                spinner.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                listRoom.setVisibility(View.VISIBLE); //display room list
                btnReset.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                displayRoom = true; //display room
                Search = false; //room cannot do search, so false
                new MainPageByDoctorInBackground().execute();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainPageByDoctor.this, AddPatientDetail.class);
                startActivity(intent);
            }
        });
        btnReset = (ImageButton) findViewById(R.id.ButtonReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search = false;
                new MainPageByDoctorInBackground().execute();
            }
        });
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(MainPageByDoctor.this, android.R.layout.simple_spinner_dropdown_item, patientColumn);
        spinner.setAdapter(lunchList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SearchType = patientColumn[position]; //when doing searching, get spinner ID
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        new MainPageByDoctorInBackground().execute();
        listview.setOnItemClickListener(listener);
        listRoom.setOnItemClickListener(listenerRoom);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //when click search
                Search = true;
                SearchValue = query;
                new MainPageByDoctorInBackground().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private class MainPageByDoctorInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainPageByDoctor.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        protected String doInBackground(String... countTo) {
            if (!Search) {
                //dispaly data
                HttpURLConnection conn;
                URL url = null;
                try {
                    if (displayRoom) { //search room
                        url = new URL("https://leo1997.000webhostapp.com/php2/ListRoomNumGrounpByRoomNum.php");
                    } else { //search doctor
                        url = new URL("https://leo1997.000webhostapp.com/php2/ListPatientDetailBySearch.php");
                    }
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
                            .appendQueryParameter("type", "departmentID")
                            .appendQueryParameter("value", SearchDepartmentID);
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
            } else {
                //do searching
                HttpURLConnection conn;
                URL url = null;
                try {
                    url = new URL("https://leo1997.000webhostapp.com/php/docter.php");
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
                            .appendQueryParameter("type", SearchType)
                            .appendQueryParameter("value", SearchValue);
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
        }

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equals("false")) {
                Toast toast = Toast.makeText(MainPageByDoctor.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                Search = false;
                return;
            }
            super.onPreExecute();
            if (displayRoom) {
                try {
                    JSONArray dataArray = new JSONArray(result);
                    roomnum = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        roomnum[i] = dataArray.getJSONObject(i).getString("roomnum");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(MainPageByDoctor.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                try {
                    JSONArray dataArray = new JSONArray(result);
                    id = new String[dataArray.length()];
                    firstname = new String[dataArray.length()];
                    lastname = new String[dataArray.length()];
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
                    Toast toast = Toast.makeText(MainPageByDoctor.this, "Error", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            if (displayRoom) {
                ArrayList<String> list = new ArrayList();
                for (int i = 0; i < roomnum.length; i++) {
                    list.add("roomnum ID: " + roomnum[i]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainPageByDoctor.this, android.R.layout.simple_expandable_list_item_1, list);
                listRoom.setAdapter(adapter);
            } else {
                List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                for (int index = 0; index < id.length; index++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id_name", "ID: " + id[index]);
                    map.put("name", "Name: " + firstname[index] + " " + lastname[index]);
                    map.put("bed_room_number", "Room: " + bedRoomNumber[index]);
                    data.add(map);
                    Adapter adapter = new Adapter(MainPageByDoctor.this, data);
                    listview.setAdapter(adapter);
                }

            }
        }
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
            String id1 = id[(int) arg3];
            String firstname1 = firstname[(int) arg3];
            String lastname1 = lastname[(int) arg3];
            String bedRoomNumber1 = bedRoomNumber[(int) arg3];
            String address1 = address[(int) arg3];
            String tel1 = tel[(int) arg3];
            String email1 = email[(int) arg3];
            String age1 = age[(int) arg3];
            String departmentID1 = departmentID[(int) arg3];
            String precautions1 = precautions[(int) arg3];

            Intent intent = new Intent();
            intent.setClass(MainPageByDoctor.this, ListPatientDetailByDoctor.class);
            Bundle bundle = new Bundle();
            bundle.putString("DoctorID", DoctorID);
            bundle.putString("id", id1);
            bundle.putString("firstname", firstname1);
            bundle.putString("lastname", lastname1);
            bundle.putString("bedRoomNumber", bedRoomNumber1);
            bundle.putString("address", address1);
            bundle.putString("tel", tel1);
            bundle.putString("email", email1);
            bundle.putString("age", age1);
            bundle.putString("departmentID", departmentID1);
            bundle.putString("precautions", precautions1);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private ListView.OnItemClickListener listenerRoom = new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent();
            intent.setClass(MainPageByDoctor.this, ListRoomDetailByDoctor.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", roomnum[(int) arg3]);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() { //Update when returning to the page again
        super.onResume();
        new MainPageByDoctorInBackground().execute();
    }
}
