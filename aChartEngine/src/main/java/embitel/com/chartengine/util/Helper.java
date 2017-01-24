package embitel.com.chartengine.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import embitel.com.chartengine.GraphicalView;

/**
 * Created by emb-sadabir on 12/10/16.
 */
public class Helper {

    public static final String TAG = "Helper";
    private static Helper ourInstance = new Helper();
    private List<Float> points;
    private int clickedPointIndex = -1;
    private GraphicalView chart;
    private int chartY;

    private Helper() {
    }

    public static Helper getInstance() {
        return ourInstance;
    }

    public void drawCircle(int index, Canvas canvas, Paint paint, float x, float y, float size, float pointStrokeWidth) {

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(pointStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, size, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#323A47"));
        canvas.drawCircle(x, y, size - 2, paint);

        if (index == clickedPointIndex) {
            paint.setColor(Color.parseColor("#ffb74d"));
            paint.setStrokeWidth(pointStrokeWidth + 2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x, y, size, paint);

            // draw filled circle
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(x, y, size - 2, paint);

            // draw line
          /*  paint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
            paint.setColor(Color.parseColor("#ffb74d"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5f);
            canvas.drawLine(x,chart.getY(),x,y-size,paint);
            paint.setPathEffect(new DashPathEffect(new float[]{0, 0}, 0));
            Log.d(TAG, "drawCircle: chartY: "+chart.getY());*/
        }
    }

    public float getPointXValue(int pointIndex) {
        List<Float> pointList = new ArrayList<>();
        for (int i = 0; i < points.size(); i += 2) {
            pointList.add(points.get(i));
        }
        return pointList.get(pointIndex);
    }

    public float getPointYValue(int pointIndex) {
        List<Float> pointList = new ArrayList<>();
        for (int i = 0; i < points.size(); i += 2) {
            pointList.add(points.get(i + 1));
        }
        return pointList.get(pointIndex);
    }

    public void setPoints(List<Float> points) {
        this.points = points;
    }

    public void setClickedPointIndex(int clickedPointIndex) {
        this.clickedPointIndex = clickedPointIndex;
    }

    public void setChart(GraphicalView chart) {
        this.chart = chart;
    }

    public void setChartY(int chartY) {
        this.chartY = chartY;
    }

    public int getChartY() {
        return chartY;
    }
}
