package com.example.fyp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

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

public class ListRoomDetailByDoctor extends AppCompatActivity implements OnChartGestureListener {
    String selectedRoomNum;
    String[] roomDetailID, humidity, temperature, isSave, roomnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_room_detail_by_doctor);
        Bundle bundle = getIntent().getExtras();
        selectedRoomNum = bundle.getString("id");
        new ListRoomDetailByDoctorInBackground().execute();
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    private class ListRoomDetailByDoctorInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ListRoomDetailByDoctor.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        protected String doInBackground(String... countTo) {
            HttpURLConnection conn;
            URL url = null;
            try {
                url = new URL("https://leo1997.000webhostapp.com/php2/ListRoomDetailByRoomNum.php");
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
                        .appendQueryParameter("roomnum", selectedRoomNum);
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

        public void onPostExecute(String result) {
            pdLoading.dismiss();
            if (result.equals("false")) {
                Toast toast = Toast.makeText(ListRoomDetailByDoctor.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            int data = 0;
            super.onPreExecute();
            try {
                JSONArray dataArray = new JSONArray(result);
                roomDetailID = new String[dataArray.length()];
                humidity = new String[dataArray.length()];
                temperature = new String[dataArray.length()];
                isSave = new String[dataArray.length()];
                roomnum = new String[dataArray.length()];
                for (int i = 0; i < dataArray.length(); i++) {
                    roomDetailID[i] = dataArray.getJSONObject(i).getString("id");
                    humidity[i] = dataArray.getJSONObject(i).getString("humidity");
                    temperature[i] = dataArray.getJSONObject(i).getString("temperature");
                    isSave[i] = dataArray.getJSONObject(i).getString("isSave");
                    roomnum[i] = dataArray.getJSONObject(i).getString("roomnum");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(ListRoomDetailByDoctor.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            LineChart line_humidity = (LineChart) findViewById(R.id.line_humidity);
            setLineChart(line_humidity);
            loadLineChartData_humidity(line_humidity);
            LineChart line_temperature = (LineChart) findViewById(R.id.line_temperature);
            setLineChart(line_temperature);
            loadLineChartData_temperature(line_temperature);
        }
    }

    private void setLineChart(LineChart chart) {
        chart.setOnChartGestureListener(this);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setAutoScaleMinMaxEnabled(false);
        // Set the X axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //Set the position of the X axis
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);
        // legend
        Legend legend = chart.getLegend();
        legend.setTextSize(10f);
        legend.setFormSize(18f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setForm(Legend.LegendForm.LINE);
        //Get the left side axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(18f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //Set the right axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false); // Right axis
        rightAxis.setDrawLabels(false); //Right axis array Label
    }


    private void loadLineChartData_humidity(final LineChart chart) {
        //List of all lines
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < humidity.length; i++) {
            //Entry(yValue, xIndex); an Entry represents a point, the first parameter is the y value, and the second is the angle of the X-axis List.
            int humidity1 = (int) (Float.parseFloat(humidity[i]) * 100);
            humidity1 = humidity1 / 100;
            entryList.add(new Entry(humidity1, i));
        }
        //LineDataSet can be seen as a line
        LineDataSet dataSet = new LineDataSet(entryList, "dataLine");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleSize(4.5f);
        dataSet.setHighLightColor(Color.RED); //Set the color of the two lines when you click on a point
        dataSet.setDrawValues(false); // Whether to draw a Value on the point
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(24f);
        allLinesList.add(dataSet);
        //LineData represents all the data of a LineChart (that is, the data of all the polylines in a LineChart)
        LineData mChartData = new LineData(getXAxisShowLable_humidity(), allLinesList);
        // set data
        chart.setData((LineData) mChartData);
        chart.setVisibleYRangeMaximum(50f, YAxis.AxisDependency.LEFT);
        chart.moveViewToY(60, YAxis.AxisDependency.LEFT);
    }

    private ArrayList<String> getXAxisShowLable_humidity() {
        ArrayList<String> ArrayList = new ArrayList<String>();
        for (int i = 0; i < roomDetailID.length; i++) {
            ArrayList.add("");
        }
        return ArrayList;
    }

    private void loadLineChartData_temperature(final LineChart chart) {
        //List of all lines
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < temperature.length; i++) {
            //Entry(yValue, xIndex); an Entry represents a point, the first parameter is the y value, and the second is the angle of the X-axis List.
            int a = (int) (Float.parseFloat(temperature[i]) * 100);
            a = a / 100;
            entryList.add(new Entry(a, i));
        }
        //LineDataSet可以看做是一条线
        LineDataSet dataSet = new LineDataSet(entryList, "dataLine");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleSize(4.5f);
        dataSet.setHighLightColor(Color.RED); //Set the color of the two lines when you click on a point
        dataSet.setDrawValues(false); // Whether to draw a Value on the point
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(24f);
        allLinesList.add(dataSet);
        //LineData represents all the data of a LineChart (that is, the data of all the polylines in a LineChart)
        LineData mChartData = new LineData(getXAxisShowLable_temperature(), allLinesList);
        // set data
        chart.setData((LineData) mChartData);
        chart.setVisibleYRangeMaximum(50f, YAxis.AxisDependency.LEFT);
        chart.moveViewToY(60, YAxis.AxisDependency.LEFT);
    }

    private ArrayList<String> getXAxisShowLable_temperature() {
        ArrayList<String> ArrayList = new ArrayList<String>();
        for (int i = 0; i < roomDetailID.length; i++) {
            ArrayList.add("");
        }
        return ArrayList;
    }
}

