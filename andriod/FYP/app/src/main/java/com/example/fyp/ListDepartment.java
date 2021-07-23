package com.example.fyp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListDepartment implements Serializable {
    String[] departmentName;
    String[] department;
    boolean haveData = false;

    public boolean haveData() {
        return haveData; //return access data
    }

    public String[] getDepartmentName() {
        return departmentName; //Return department name
    }

    public String[] getDepartmentID() {
        return department; //Return department id
    }

    public void getData() {
        new RestAsyncTask1().execute(); //loading department information
    }

    public class RestAsyncTask1 extends AsyncTask<String, Void, String> {
        HttpURLConnection conn;
        URL url = null;
        String departmentData;

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection conn = null;
                URL url = new URL("https://leo1997.000webhostapp.com/php2/ListDepartment.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                departmentData = reader.readLine();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
            return departmentData;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                haveData = false; //return null
                return;
            } else {
                try {
                    JSONArray dataArray = new JSONArray(result);
                    department = new String[dataArray.length()];
                    departmentName = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        department[i] = dataArray.getJSONObject(i).getString("id");
                        departmentName[i] = dataArray.getJSONObject(i).getString("departmentName");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    haveData = false; //return null
                    return;
                }
                haveData = true;
            }
        }
    }
}
