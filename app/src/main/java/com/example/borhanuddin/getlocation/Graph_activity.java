package com.example.borhanuddin.getlocation;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;
import java.util.List;

public class Graph_activity extends AppCompatActivity implements SensorEventListener {

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private Runnable mTimer3;
    private GraphView graphView1;
    private GraphView graphView2;
    private GraphView graphView3;
    private GraphViewSeries exampleSeries1;
    private GraphViewSeries exampleSeries2;
    private GraphViewSeries exampleSeries3;
    private double sensorX = 0.0;
    private double sensorY = 0.0;
    private double sensorZ = 0.0;
    private List<GraphView.GraphViewData> seriesX;
    private List<GraphView.GraphViewData> seriesY;
    private List<GraphView.GraphViewData> seriesZ;
    int dataCount = 1;

    //the Sensor Manager
    private SensorManager sManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);

        seriesX = new ArrayList<GraphView.GraphViewData>();
        seriesY = new ArrayList<GraphView.GraphViewData>();
        seriesZ = new ArrayList<GraphView.GraphViewData>();

        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // init example series data
        exampleSeries1 = new GraphViewSeries(new GraphView.GraphViewData[] {});

        graphView1 = new LineGraphView(
                this // context
                , "X axis" // heading
        );

        graphView1.addSeries(exampleSeries1); // data
        LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
        layout.addView(graphView1);

        graphView1.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView1.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView1.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView1.getGraphViewStyle().setNumHorizontalLabels(5);
        graphView1.getGraphViewStyle().setNumVerticalLabels(4);
        graphView1.getGraphViewStyle().setVerticalLabelsWidth(150);


        // ----------
        exampleSeries2 = new GraphViewSeries(new GraphView.GraphViewData[] {});

        graphView2 = new LineGraphView(
                this
                , "Y axis"
        );
        //((LineGraphView) graphView).setDrawBackground(true);

        graphView2.addSeries(exampleSeries2); // data
        layout = (LinearLayout) findViewById(R.id.graph2);
        layout.addView(graphView2);

        graphView2.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView2.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView2.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView2.getGraphViewStyle().setNumHorizontalLabels(5);
        graphView2.getGraphViewStyle().setNumVerticalLabels(4);
        graphView2.getGraphViewStyle().setVerticalLabelsWidth(150);


        // init example series data
        exampleSeries3 = new GraphViewSeries(new GraphView.GraphViewData[] {});

        graphView3 = new LineGraphView(
                this // context
                , "Z axis" // heading
        );

        graphView3.addSeries(exampleSeries3); // data
        layout = (LinearLayout) findViewById(R.id.graph3);
        layout.addView(graphView3);
        graphView3.getGraphViewStyle().setGridColor(Color.BLACK);
        graphView3.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView3.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView3.getGraphViewStyle().setNumHorizontalLabels(5);
        graphView3.getGraphViewStyle().setNumVerticalLabels(4);
        graphView3.getGraphViewStyle().setVerticalLabelsWidth(150);

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {

        sensorX = event.values[2];
        sensorY = event.values[1];
        sensorZ = event.values[0];

        seriesX.add(new GraphView.GraphViewData(dataCount, sensorX));
        seriesY.add(new GraphView.GraphViewData(dataCount, sensorY));
        seriesZ.add(new GraphView.GraphViewData(dataCount, sensorZ));

        dataCount++;


/*		Context context = getApplicationContext();
		float number = (float)Math.round(sensorX * 1000) / 1000;
		//string formattedNumber = Float.toString(number);
		CharSequence text = Float.toString(number);
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
*/
        if (seriesX.size() > 500) {
            seriesX.remove(0);
            seriesY.remove(0);
            seriesZ.remove(0);
            graphView1.setViewPort(dataCount - 500, 500);
            graphView2.setViewPort(dataCount - 500, 500);
            graphView3.setViewPort(dataCount - 500, 500);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);

        mTimer1 = new Runnable() {
            @Override
            public void run() {
                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesX.size()];
                seriesX.toArray(gvd);
                exampleSeries1.resetData(gvd);
                mHandler.post(this);//100);
            }
        };
        mHandler.postDelayed(mTimer1, 100);

        mTimer2 = new Runnable() {
            @Override
            public void run() {

                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesY.size()];
                seriesY.toArray(gvd);
                exampleSeries2.resetData(gvd);

                mHandler.post(this);
            }
        };
        mHandler.postDelayed(mTimer2, 100);


        mTimer3 = new Runnable() {
            @Override
            public void run() {

                GraphView.GraphViewData[] gvd = new GraphView.GraphViewData[seriesZ.size()];
                seriesZ.toArray(gvd);
                exampleSeries3.resetData(gvd);

                mHandler.post(this);
            }
        };
        mHandler.postDelayed(mTimer3, 100);
    }
}
