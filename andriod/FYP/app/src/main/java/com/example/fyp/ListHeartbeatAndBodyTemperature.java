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

public class ListHeartbeatAndBodyTemperature extends AppCompatActivity implements OnChartGestureListener {
    ArrayList<String> TempID = new ArrayList();
    ArrayList<String> HBID = new ArrayList();
    String Temp[];
    String HB[];
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_heartbeat_and_bodytemperature);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        new ListBodyTemperatureInBackground().execute();
        new ListHeartbeatInBackground().execute();
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

    private class ListBodyTemperatureInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ListHeartbeatAndBodyTemperature.this);

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
                url = new URL("https://leo1997.000webhostapp.com/php2/ListBodyTemperatureByPatientID.php");
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
                        .appendQueryParameter("id", id);
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
                Toast toast = Toast.makeText(ListHeartbeatAndBodyTemperature.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            int data = 0;
            super.onPreExecute();
            try {
                JSONArray dataArray = new JSONArray(result);
                Temp = new String[dataArray.length()];
                for (int i = 0; i < dataArray.length(); i++) {
                    TempID.add(Integer.toString(data));
                    Temp[i] = dataArray.getJSONObject(i).getString("bodyTemperature");
                    data++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(ListHeartbeatAndBodyTemperature.this, "JSONException", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            // create diagram
            LineChart line_Temp = (LineChart) findViewById(R.id.line_Temp);
            setLineChart(line_Temp);
            loadLineChartData_Temp(line_Temp);
        }
    }

    private class ListHeartbeatInBackground extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ListHeartbeatAndBodyTemperature.this);

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
                url = new URL("https://leo1997.000webhostapp.com/php2/ListHeartbeatByPatientID.php");
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
                        .appendQueryParameter("id", id);
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
                Toast toast = Toast.makeText(ListHeartbeatAndBodyTemperature.this, "no data", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            super.onPreExecute();
            int data = 0;
            try {
                JSONArray dataArray = new JSONArray(result);
                HB = new String[dataArray.length()];
                for (int i = 0; i < dataArray.length(); i++) {
                    HBID.add(Integer.toString(data));
                    HB[i] = dataArray.getJSONObject(i).getString("heartbeat");
                    data++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(ListHeartbeatAndBodyTemperature.this, "Error", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            // create diagram
            LineChart line_HB = (LineChart) findViewById(R.id.line_HB);
            setLineChart(line_HB);
            loadLineChartData_HB(line_HB);
        }
    }

    private void setLineChart(LineChart chart) {
        chart.setOnChartGestureListener(this);
        chart.setDrawGridBackground(false); // Set the grid background
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setScaleEnabled(false); // 设置缩放
        chart.setDoubleTapToZoomEnabled(false); // Set double click without zooming
        chart.setAutoScaleMinMaxEnabled(false);
        // Set the X axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set the position of the X axis
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
        // Get the left side axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextSize(18f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //Set the right axis
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false); // Right axis
        rightAxis.setDrawLabels(false); // Right axis array Label
    }


    private void loadLineChartData_Temp(final LineChart chart) {
        //List of all lines
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < TempID.size(); i++) {
            //Entry(yValue,xIndex); An Entry represents a point, the first parameter is the y value, and the second is the angle of the X-axis List.
            int a = (int) (Float.parseFloat(Temp[i]) * 100);
            a = a / 100;
            entryList.add(new Entry(a, i));
        }
        //LineDataSet can be seen as a line
        LineDataSet dataSet = new LineDataSet(entryList, "dataLine");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleSize(4.5f);
        dataSet.setHighLightColor(Color.RED); // Set the color of the two lines when you click on a point
        dataSet.setDrawValues(false); // Whether to draw a Value on the point
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(24f);
        allLinesList.add(dataSet);
        //LineData represents all the data of a LineChart (that is, the data of all the polylines in a LineChart)
        LineData mChartData = new LineData(getXAxisShowLable_Temp(), allLinesList);
        // set data
        chart.setData((LineData) mChartData);
        chart.setVisibleYRangeMaximum(50f, YAxis.AxisDependency.LEFT);
        chart.moveViewToY(60, YAxis.AxisDependency.LEFT);
    }

    private ArrayList<String> getXAxisShowLable_Temp() {
        ArrayList<String> m = new ArrayList<String>();
        for (int i = 0; i < TempID.size(); i++) {
            m.add("");
        }
        return m;
    }

    private void loadLineChartData_HB(final LineChart chart) {
        //List of all lines
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < HBID.size(); i++) {
            //Entry(yValue, xIndex); an Entry represents a point, the first parameter is the y value, and the second is the angle of the X-axis List.
            int a = Integer.parseInt(HB[i]);
            entryList.add(new Entry(a, i));
        }
        //LineDataSet can be seen as a line
        LineDataSet dataSet = new LineDataSet(entryList, "dataLine");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleSize(4.5f);
        dataSet.setHighLightColor(Color.RED); // Set the color of the two lines when you click on a point
        dataSet.setDrawValues(false); // Whether to draw a Value on the point
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setValueTextSize(24f);
        allLinesList.add(dataSet);
        //LineData represents all the data of a LineChart (that is, the data of all the polylines in a LineChart)
        LineData mChartData = new LineData(getXAxisShowLable_HB(), allLinesList);
        // set data
        chart.setData((LineData) mChartData);
        chart.setVisibleYRangeMaximum(50f, YAxis.AxisDependency.LEFT);
        chart.moveViewToY(60, YAxis.AxisDependency.LEFT);
    }

    private ArrayList<String> getXAxisShowLable_HB() {
        ArrayList<String> m = new ArrayList<String>();
        for (int i = 0; i < HBID.size(); i++) {
            m.add("");
        }
        return m;
    }
}
