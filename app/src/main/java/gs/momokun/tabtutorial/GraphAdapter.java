package gs.momokun.tabtutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

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
    CheckBox cb_temp;
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
        cb_temp = (CheckBox) view.findViewById(R.id.checkbox_temp);
        cb_temp.setChecked(true);

        series2 = new LineGraphSeries<>(generateData());

            graph.addSeries(series2);
            graph.setTitle("Usage");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getLegendRenderer().setSpacing(15);
            series2.setTitle("Temperature");
            series2.setColor(Color.BLUE);
            series2.setDrawDataPoints(true);
            series2.setDataPointsRadius(10);
            series2.setThickness(5);






        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        cb_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_temp.isChecked()){
                    graph.addSeries(series2);
                    graph.getLegendRenderer().setVisible(true);
                    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                    graph.getLegendRenderer().setSpacing(15);

                    series2.setTitle("Temperature");
                    series2.setColor(Color.BLUE);
                    series2.setDrawDataPoints(true);
                    series2.setDataPointsRadius(10);
                    series2.setThickness(5);

                }else if(!cb_temp.isChecked()){
                    graph.removeSeries(series2);
                    graph.getLegendRenderer().setVisible(false);


                }
            }
        });

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

    private DataPoint[] generateData2() {
        int count = 312;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = i;
            double y = f*15-mRand.nextDouble()+150;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;

        }
        return values;
    }

    Random mRand = new Random();
}
