package gs.momokun.tabtutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

/**
 * Created by ElmoTan on 12/7/2016.
 */

public class GraphAdapter {
    DatabaseHandler db;
    OneFragment of;
    Activity activity;
    Context context;
    View view;
    LineGraphSeries<DataPoint> series2;
    GraphView graph;
    public GraphAdapter() {

    }

    public GraphAdapter(Activity activity, Context context, View view) {
        this.activity=activity;
        this.context=context;
        this.view=view;
    }

    public void viewGraph(){
        db = new DatabaseHandler(context);
        graph = new GraphView(context);
        graph = (GraphView) view.findViewById(R.id.graph);
        series2 = new LineGraphSeries<>(generateData());
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData());
        //  graph.addSeries(series);
        if(series2!=null) {
            graph.addSeries(series2);
            graph.setTitle("Example");

            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getLegendRenderer().setSpacing(15);


        /*series.setTitle("Temp");
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(5);*/

            series2.setTitle("Watt");
            series2.setColor(Color.BLUE);
            series2.setDrawDataPoints(true);
            series2.setDataPointsRadius(10);
            series2.setThickness(5);

// custom paint to make a dotted line
            // enable scaling and scrolling
        graph.getViewport().setScalable(true);
                  graph.getViewport().setScalableY(true);
        }
    }

    private DataPoint[] generateData() {
        int count = 0;

        List<DataLogging> contacts = db.getAllContacts();
        for(DataLogging x : contacts){
            count++;
        }
        int i = 0;
        DataPoint[] values = new DataPoint[count];
        for (DataLogging cn : contacts) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_date() + " ,Phone: " + cn.get_temp();
            // Writing Contacts to log
            double x = i;
            double y = Double.parseDouble(cn.get_temp());
            DataPoint v = new DataPoint(x, y);
            Log.d("Name: ", log);
            values[i] = v;
            i++;
        }


        return values;
    }
}
