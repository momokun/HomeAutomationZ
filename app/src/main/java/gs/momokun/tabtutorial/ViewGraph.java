package gs.momokun.tabtutorial;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ViewGraph extends AppCompatActivity {


    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        db = new DatabaseHandler(this);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<>(generateData());
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(generateData());
      //  graph.addSeries(series);
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
        int count = 32;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = i;
            double y = i+i;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;

        }
        return values;
    }

    Random mRand = new Random();

}
