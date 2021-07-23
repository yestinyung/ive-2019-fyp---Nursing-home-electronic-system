package com.example.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification_ListView extends AppCompatActivity {
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
    //Define notification length
    int index_odyTemperature;
    int index_heartbeat;
    int index_roomnum;
    int index_fall;
    //If there is no notice
    boolean have_Temperature_Data = false;
    boolean have_heartbea_Datat = false;
    boolean have_roomnum_Data = false;
    boolean have_fall_Data = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__list_view);
        Bundle bundle = getIntent().getExtras();
        id_bodyTemperature = bundle.getStringArray("id_bodyTemperature");
        bodyTemperature = bundle.getStringArray("bodyTemperature");
        patientID_bodyTemperature = bundle.getStringArray("patientID_bodyTemperature");
        isSave_bodyTemperature = bundle.getStringArray("isSave_bodyTemperature");

        id_heartbeat = bundle.getStringArray("id_heartbeat");
        heartbeat = bundle.getStringArray("heartbeat");
        patientID_heartbeat = bundle.getStringArray("patientID_heartbeat");
        isSave_heartbeat = bundle.getStringArray("isSave_heartbeat");

        id_roomnum = bundle.getStringArray("id_roomnum");
        cm1 = bundle.getStringArray("cm1");
        cm2 = bundle.getStringArray("cm2");
        humidity = bundle.getStringArray("humidity");
        temperature = bundle.getStringArray("temperature");
        isSave_roomnum = bundle.getStringArray("isSave_roomnum");
        roomnum = bundle.getStringArray("roomnum");
        firstname_bodyTemperature = bundle.getStringArray("firstname_bodyTemperature");
        lastname_bodyTemperature = bundle.getStringArray("lastname_bodyTemperature");
        firstname_heartbeat = bundle.getStringArray("firstname_heartbeat");
        lastname_heartbeat = bundle.getStringArray("lastname_heartbeat");

        id_fall = bundle.getStringArray("id_fall");
        patientID_fall = bundle.getStringArray("patientID_fall");
        isSave_fall = bundle.getStringArray("isSave_fall");

        have_Temperature_Data = bundle.getBoolean("have_Temperature_Data");
        have_heartbea_Datat = bundle.getBoolean("have_heartbea_Datat");
        have_roomnum_Data = bundle.getBoolean("have_roomnum_Data");
        have_fall_Data = bundle.getBoolean("have_fall_Data");
        id = bundle.getString("id");
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>(); //create list view array
        if (have_Temperature_Data) {
            for (int index = 0; index < id_bodyTemperature.length; index++) {//Calculate the length of the notification, upload to adapter
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id_name", "patientID: " + patientID_bodyTemperature[index]);
                map.put("name", "Name: " + firstname_bodyTemperature[index] + " " + lastname_bodyTemperature[index]);
                map.put("bed_room_number", "bodyTemperature: " + bodyTemperature[index]);
                data.add(map);
                index_odyTemperature++;//record length
            }
        }
        if (have_heartbea_Datat) {
            for (int index = 0; index < id_heartbeat.length; index++) {//Calculate the length of the notification, upload to adapter
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id_name", "patientID " + patientID_heartbeat[index]);
                map.put("name", "Name: " + firstname_heartbeat[index] + " " + lastname_heartbeat[index]);
                map.put("bed_room_number", "heartbeat: " + heartbeat[index]);
                data.add(map);
                index_heartbeat++;//record length
            }
        }
        if (have_roomnum_Data) {
            for (int index = 0; index < id_roomnum.length; index++) {//Calculate the length of the notification, upload to adapter
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id_name", "roomnum: " + roomnum[index]);
                map.put("name", "humidity: " + humidity[index]);
                map.put("bed_room_number", "temperature: " + temperature[index]);
                data.add(map);
                index_roomnum++;//record length
            }
        }
        if (have_fall_Data) {
            for (int index = 0; index < id_fall.length; index++) {//Calculate the length of the notification, upload to adapter
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id_name", "id: " + id_fall[index]);
                map.put("name", "patientID: " + patientID_fall[index]);
                map.put("bed_room_number", "isSave: " + isSave_fall[index]);
                data.add(map);
                index_fall++;//record length
            }
        }
        ListView listView = (ListView) findViewById(R.id.list);
        Adapter adapter = new Adapter(Notification_ListView.this, data);
        listView.setAdapter(adapter);//create list view
        listView.setOnItemClickListener(listener);
    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
            if (arg3 < index_odyTemperature) { //If the value of arg3 is less than the number of temperature notifications just recorded
                Intent intent = new Intent();
                intent.setClass(Notification_ListView.this, Notification_closed.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id_bodyTemperature[(int) arg3]);
                bundle.putString("Data", "bodytemperature");
                bundle.putString("Text", firstname_bodyTemperature[(int) arg3]
                        + " " + lastname_bodyTemperature[(int) arg3]
                        + "\n" + patientID_bodyTemperature[(int) arg3]
                        + "\n" + bodyTemperature[(int) arg3]
                        + "\n" + isSave_bodyTemperature[(int) arg3]);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else if (arg3 >= index_odyTemperature && arg3 < index_heartbeat + index_odyTemperature) {
                Intent intent = new Intent();
                intent.setClass(Notification_ListView.this, Notification_closed.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id_heartbeat[(int) arg3 - index_odyTemperature]);
                bundle.putString("Data", "heartbeat");
                bundle.putString("Text", firstname_heartbeat[(int) arg3 - index_odyTemperature]
                        + " " + lastname_heartbeat[(int) arg3 - index_odyTemperature]
                        + "\n" + patientID_heartbeat[(int) arg3 - index_odyTemperature]
                        + "\n" + heartbeat[(int) arg3 - index_odyTemperature]
                        + "\n" + isSave_heartbeat[(int) arg3 - index_odyTemperature]);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else if (arg3 >= index_heartbeat + index_odyTemperature && arg3 < index_heartbeat + index_odyTemperature + index_roomnum) {
                Intent intent = new Intent();
                intent.setClass(Notification_ListView.this, Notification_closed.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id_roomnum[(int) arg3 - index_heartbeat - index_odyTemperature]);
                bundle.putString("Data", "roomdetail");
                bundle.putString("Text", cm1[(int) arg3 - index_heartbeat - index_odyTemperature] + "\n" +
                        cm2[(int) arg3 - index_heartbeat - index_odyTemperature] + "\n" +
                        humidity[(int) arg3 - index_heartbeat - index_odyTemperature] + "\n" +
                        temperature[(int) arg3 - index_heartbeat - index_odyTemperature] + "\n" +
                        isSave_roomnum[(int) arg3 - index_heartbeat - index_odyTemperature] + "\n" +
                        roomnum[(int) arg3 - index_heartbeat - index_odyTemperature]);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else if (arg3 >= index_heartbeat + index_odyTemperature + index_roomnum) {

                Intent intent = new Intent();
                intent.setClass(Notification_ListView.this, Notification_closed.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id_fall[(int) arg3 - index_heartbeat - index_odyTemperature - index_roomnum]);
                bundle.putString("Data", "urgentrecord");
                bundle.putString("Text", patientID_fall[(int) arg3 - index_heartbeat - index_odyTemperature - index_roomnum] + "\n" +
                        isSave_fall[(int) arg3 - index_heartbeat - index_odyTemperature - index_roomnum]);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:    //return key
                return true;   //This will exit because of break, so we have to deal with it ourselves. We don’t return to the previous layer.
        }
        return super.onKeyDown(keyCode, event);
    }
}
