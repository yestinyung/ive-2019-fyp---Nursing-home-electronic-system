package com.example.fyp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ycbjie.notificationlib.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NickyService extends Service {
    private Handler handler = new Handler();
    String[] id_bodyTemperature;
    String[] bodyTemperature;
    String[] patientID_bodyTemperature;
    String[] isSave_bodyTemperature;
    String[] id_heartbeat;
    String[] heartbeat;
    String[] patientID_heartbeat;
    String[] isSave_heartbeat;
    String[] id_roomnum;
    String[] cm1;
    String[] cm2;
    String[] humidity;
    String[] temperature;
    String[] isSave_roomnum;
    String[] roomnum;
    String[] firstname_bodyTemperature;
    String[] lastname_bodyTemperature;
    String[] firstname_heartbeat;
    String[] lastname_heartbeat;
    String[] id_fall;
    String[] patientID_fall;
    String[] isSave_fall;
    String id;
    String back_data;
    //save already Notification data
    ArrayList<String> Yes_Notification_Temperature = new ArrayList();
    ArrayList<String> Yes_Notification_heartbeat = new ArrayList();
    ArrayList<String> Yes_Notification_roomnum = new ArrayList();
    ArrayList<String> Yes_Notification_fall = new ArrayList();
    // if need to Notification
    boolean have_Notification_Temperature = false;
    boolean have_Notification_heartbeat = false;
    boolean have_Notification_roomnum = false;
    boolean have_Notification_fall = false;
    // if have data
    boolean have_Temperature_Data = false;
    boolean have_heartbea_Datat = false;
    boolean have_roomnum_Data = false;
    boolean have_fall_Data = false;
    String NotificationText = "";
    String NotificationText2 = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //Restart background execution
        id = intent.getStringExtra("input");
        back_data = intent.getStringExtra("back_data");
        /*
                        Return to the doctor to confirm the data, to delete the information stored in the notification, so that the same ID notification is accepted again.
                */
        for (int i = 0; i < Yes_Notification_Temperature.size(); i++) {
            if (Yes_Notification_Temperature.get(i).equals(back_data)) {
                Yes_Notification_Temperature.remove(i); //temperature high Notification
            }
        }
        for (int i = 0; i < Yes_Notification_heartbeat.size(); i++) {
            if (Yes_Notification_heartbeat.get(i).equals(back_data)) {
                Yes_Notification_heartbeat.remove(i); //heartbeat high Notification
            }
        }
        for (int i = 0; i < Yes_Notification_roomnum.size(); i++) {
            if (Yes_Notification_roomnum.get(i).equals(back_data)) {
                Yes_Notification_roomnum.remove(i); //abnormal ward room.
            }
        }
        for (int i = 0; i < Yes_Notification_fall.size(); i++) {
            if (Yes_Notification_fall.get(i).equals(back_data)) {
                Yes_Notification_fall.remove(i); //fall  Notification
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(showTime, 2000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            new NickyService.GoodTask().execute();
            handler.postDelayed(this, 2000);//do each 2 second
        }
    };

    class GoodTask extends AsyncTask<String, Integer, String> {


        String jsonString1 = "";

        @Override
        protected String doInBackground(String... countTo) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                URL url = new URL("https://leo1997.000webhostapp.com/php/CheckIsSave.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                jsonString1 = reader.readLine();
                reader.close();
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                if (jsonString1.equals("")) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "網路中斷" + e;
            }
            return jsonString1;
        }

        public void onPostExecute(String result) {
            super.onPreExecute();
            try {
                String eng = result;
                /*
                                    If there is no notification at all = [false[]false][false][false]
                                    so we using ][ cutting data
                                    if there is a notice inside, it is a json.
                                */
                String[] array = eng.split("]\\[");
                for (int i = 0; i < array.length; i++) {
                    System.out.println("array[" + i + "] = " + array[i]);
                }
                if (!(array[0].equals("[false"))) { //get [false becasue cutting data by ][
                    have_Temperature_Data = true; //Representative has notification data
                    JSONArray dataArray = new JSONArray(array[0] + "]"); // insert a ] to let it become a json
                    id_bodyTemperature = new String[dataArray.length()];
                    bodyTemperature = new String[dataArray.length()];
                    patientID_bodyTemperature = new String[dataArray.length()];
                    isSave_bodyTemperature = new String[dataArray.length()];
                    firstname_bodyTemperature = new String[dataArray.length()];
                    lastname_bodyTemperature = new String[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {
                        id_bodyTemperature[i] = dataArray.getJSONObject(i).getString("id");
                        bodyTemperature[i] = dataArray.getJSONObject(i).getString("bodyTemperature");
                        patientID_bodyTemperature[i] = dataArray.getJSONObject(i).getString("patientID");
                        isSave_bodyTemperature[i] = dataArray.getJSONObject(i).getString("isSave");
                        firstname_bodyTemperature[i] = dataArray.getJSONObject(i).getString("firstname");
                        lastname_bodyTemperature[i] = dataArray.getJSONObject(i).getString("lastname");
                    }
                }
                if (!(array[1].equals("false"))) { //get [false becasue cutting data by ][
                    have_heartbea_Datat = true; //Representative has notification data
                    JSONArray dataArray2 = new JSONArray("[" + array[1] + "]"); // insert a [] to let it become a json
                    id_heartbeat = new String[dataArray2.length()];
                    heartbeat = new String[dataArray2.length()];
                    patientID_heartbeat = new String[dataArray2.length()];
                    isSave_heartbeat = new String[dataArray2.length()];
                    firstname_heartbeat = new String[dataArray2.length()];
                    lastname_heartbeat = new String[dataArray2.length()];
                    for (int i = 0; i < dataArray2.length(); i++) {
                        id_heartbeat[i] = dataArray2.getJSONObject(i).getString("id");
                        heartbeat[i] = dataArray2.getJSONObject(i).getString("heartbeat");
                        patientID_heartbeat[i] = dataArray2.getJSONObject(i).getString("patientID");
                        isSave_heartbeat[i] = dataArray2.getJSONObject(i).getString("isSave");
                        firstname_heartbeat[i] = dataArray2.getJSONObject(i).getString("firstname");
                        lastname_heartbeat[i] = dataArray2.getJSONObject(i).getString("lastname");
                    }
                }
                if (!(array[2].equals("false"))) {
                    have_roomnum_Data = true; //Representative has notification data
                    JSONArray dataArray3 = new JSONArray("[" + array[2] + "]");
                    id_roomnum = new String[dataArray3.length()];
                    cm1 = new String[dataArray3.length()];
                    cm2 = new String[dataArray3.length()];
                    humidity = new String[dataArray3.length()];
                    temperature = new String[dataArray3.length()];
                    isSave_roomnum = new String[dataArray3.length()];
                    roomnum = new String[dataArray3.length()];
                    for (int i = 0; i < dataArray3.length(); i++) {
                        id_roomnum[i] = dataArray3.getJSONObject(i).getString("id");
                        cm1[i] = dataArray3.getJSONObject(i).getString("cm1");
                        cm2[i] = dataArray3.getJSONObject(i).getString("cm2");
                        humidity[i] = dataArray3.getJSONObject(i).getString("humidity");
                        temperature[i] = dataArray3.getJSONObject(i).getString("temperature");
                        isSave_roomnum[i] = dataArray3.getJSONObject(i).getString("isSave");
                        roomnum[i] = dataArray3.getJSONObject(i).getString("roomnum");
                    }
                }
                if (!(array[3].equals("false]"))) { //get [false becasue cutting data by ][
                    have_fall_Data = true; //Representative has notification data
                    JSONArray dataArray4 = new JSONArray("[" + array[3]); // insert a [ to let it become a json
                    id_fall = new String[dataArray4.length()];
                    patientID_fall = new String[dataArray4.length()];
                    isSave_fall = new String[dataArray4.length()];
                    for (int i = 0; i < dataArray4.length(); i++) {
                        id_fall[i] = dataArray4.getJSONObject(i).getString("id");
                        patientID_fall[i] = dataArray4.getJSONObject(i).getString("patientID");
                        isSave_fall[i] = dataArray4.getJSONObject(i).getString("isSave");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            if (have_Temperature_Data) { //if have notification
                for (int i = 0; i < id_bodyTemperature.length; i++) {// Number of notifications
                    boolean isIn = Yes_Notification_Temperature.contains(id_bodyTemperature[i]); //search is there a already notification data
                    if (!isIn) {
                        Yes_Notification_Temperature.add(id_bodyTemperature[i]); //confirm notification
                        have_Notification_Temperature = true;
                        NotificationText2 = NotificationText2 + "bodyTemperature patientID :" + patientID_bodyTemperature[i] + " \n"; //notification message with patient ID
                    }
                }
            }
            if (have_heartbea_Datat) {//if have notification
                for (int i = 0; i < id_heartbeat.length; i++) {// Number of notifications
                    boolean isIn = Yes_Notification_heartbeat.contains(id_heartbeat[i]);//search is there a already notification data
                    if (!isIn) {
                        Yes_Notification_heartbeat.add(id_heartbeat[i]);//confirm notification
                        have_Notification_heartbeat = true;
                        NotificationText2 = NotificationText2 + "heartbeat patientID :" + patientID_heartbeat[i] + " \n";//notification message with patient ID
                    }
                }
            }
            if (have_roomnum_Data) {//if have notification
                for (int i = 0; i < id_roomnum.length; i++) {// Number of notifications
                    boolean isIn = Yes_Notification_roomnum.contains(id_roomnum[i]);//search is there a already notification data
                    if (!isIn) {
                        Yes_Notification_roomnum.add(id_roomnum[i]);//confirm notification
                        have_Notification_roomnum = true;
                        NotificationText2 = NotificationText2 + "roomnum :" + id_roomnum[i] + " \n";//notification message with patient ID
                    }
                }
            }
            if (have_fall_Data) {//if have notification
                for (int i = 0; i < id_fall.length; i++) {// Number of notifications
                    boolean isIn = Yes_Notification_fall.contains(id_fall[i]);//search is there a already notification data
                    if (!isIn) {
                        Yes_Notification_fall.add(id_fall[i]);//confirm notification
                        have_Notification_fall = true;
                        NotificationText2 = NotificationText2 + "fall patientID:" + patientID_fall[i] + " \n";//notification message with patient ID
                    }
                }
            }
            if (have_Notification_heartbeat) {
                NotificationText = NotificationText + "Abnormal heartbeat";
            }
            if (have_Notification_Temperature) {
                if (have_Notification_heartbeat) {
                    NotificationText = NotificationText + " & ";
                }
                NotificationText = NotificationText + "Abnormal body temperature";
            }
            if (have_Notification_roomnum) {
                if (have_Notification_Temperature) {
                    NotificationText = NotificationText + " & ";
                }
                NotificationText = NotificationText + "Abnormal ward";
            }
            if (have_Notification_fall) {
                if (have_Notification_roomnum) {
                    NotificationText = NotificationText + " & ";
                }
                NotificationText = NotificationText + "fall";
            }
            if (have_Notification_heartbeat || have_Notification_Temperature || have_Notification_roomnum || have_Notification_fall) {
                /*Bundle bundle = new Bundle();
                bundle.putStringArray("id_bodyTemperature",id_bodyTemperature);
                bundle.putStringArray("bodyTemperature",bodyTemperature);
                bundle.putStringArray("patientID_bodyTemperature",patientID_bodyTemperature);
                bundle.putStringArray("isSave_bodyTemperature",isSave_bodyTemperature);
                bundle.putStringArray("id_heartbeat",id_heartbeat);
                bundle.putStringArray("heartbeat",heartbeat);
                bundle.putStringArray("patientID_heartbeat",patientID_heartbeat);
                bundle.putStringArray("isSave_heartbeat",isSave_heartbeat);
                bundle.putStringArray("id_roomnum",id_roomnum);
                bundle.putStringArray("cm1",cm1);
                bundle.putStringArray("cm2",cm2);
                bundle.putStringArray("humidity",humidity);
                bundle.putStringArray("temperature",temperature);
                bundle.putStringArray("isSave_roomnum",isSave_roomnum);
                bundle.putStringArray("roomnum",roomnum);
                bundle.putStringArray("firstname_bodyTemperature",firstname_bodyTemperature);
                bundle.putStringArray("lastname_bodyTemperature",lastname_bodyTemperature);
                bundle.putStringArray("firstname_heartbeat",firstname_heartbeat);
                bundle.putStringArray("lastname_heartbeat",lastname_heartbeat);
                bundle.putStringArray("id_fall",id_fall);
                bundle.putStringArray("patientID_fall",patientID_fall);
                bundle.putStringArray("isSave_fall",isSave_fall);
                bundle.putString("Doctor_id",id);
                bundle.putBoolean("have_Temperature_Data",have_Temperature_Data);
                bundle.putBoolean("have_heartbea_Datat",have_heartbea_Datat);
                bundle.putBoolean("have_roomnum_Data",have_roomnum_Data);
                bundle.putBoolean("have_fall_Data",have_fall_Data);
                Intent intent=new Intent() ;
                intent.setClass(NickyService.this,Notification_ListView.class);
                intent.putExtras(bundle);
                startActivity(intent);*/
                addNotification();
            }
            have_Notification_heartbeat = false;
            have_Notification_Temperature = false;
            have_Notification_roomnum = false;
            have_Notification_fall = false;
            have_Temperature_Data = false;
            have_heartbea_Datat = false;
            have_roomnum_Data = false;
            have_fall_Data = false;
            NotificationText2 = "";
            NotificationText = "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

    }

    private void addNotification() {
        //There are two ways to set the vibration. It is the same as setting the ringtone.
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        //Handling the logic of clicking Notification
        //Create intent
        Intent resultIntent = new Intent(this, Notification_ListView.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("id_bodyTemperature", id_bodyTemperature);
        bundle.putStringArray("bodyTemperature", bodyTemperature);
        bundle.putStringArray("patientID_bodyTemperature", patientID_bodyTemperature);
        bundle.putStringArray("isSave_bodyTemperature", isSave_bodyTemperature);
        bundle.putStringArray("id_heartbeat", id_heartbeat);
        bundle.putStringArray("heartbeat", heartbeat);
        bundle.putStringArray("patientID_heartbeat", patientID_heartbeat);
        bundle.putStringArray("isSave_heartbeat", isSave_heartbeat);
        bundle.putStringArray("id_roomnum", id_roomnum);
        bundle.putStringArray("cm1", cm1);
        bundle.putStringArray("cm2", cm2);
        bundle.putStringArray("humidity", humidity);
        bundle.putStringArray("temperature", temperature);
        bundle.putStringArray("isSave_roomnum", isSave_roomnum);
        bundle.putStringArray("roomnum", roomnum);
        bundle.putStringArray("firstname_bodyTemperature", firstname_bodyTemperature);
        bundle.putStringArray("lastname_bodyTemperature", lastname_bodyTemperature);
        bundle.putStringArray("firstname_heartbeat", firstname_heartbeat);
        bundle.putStringArray("lastname_heartbeat", lastname_heartbeat);
        bundle.putStringArray("id_fall", id_fall);
        bundle.putStringArray("patientID_fall", patientID_fall);
        bundle.putStringArray("isSave_fall", isSave_fall);
        bundle.putString("Doctor_id", id);
        bundle.putBoolean("have_Temperature_Data", have_Temperature_Data);
        bundle.putBoolean("have_heartbea_Datat", have_heartbea_Datat);
        bundle.putBoolean("have_roomnum_Data", have_roomnum_Data);
        bundle.putBoolean("have_fall_Data", have_fall_Data);
        resultIntent.putExtras(bundle);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("what", 2);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Define various properties of Notification
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(vibrate)
                .sendNotificationCompat(2, NotificationText, NotificationText2, R.mipmap.ic_launcher);
    }

}
