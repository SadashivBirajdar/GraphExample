package embitel.com.graphexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import embitel.com.chartengine.ChartFactory;
import embitel.com.chartengine.GraphicalView;
import embitel.com.chartengine.chart.PointStyle;
import embitel.com.chartengine.model.SeriesSelection;
import embitel.com.chartengine.model.TimeSeries;
import embitel.com.chartengine.model.XYMultipleSeriesDataset;
import embitel.com.chartengine.renderer.XYMultipleSeriesRenderer;
import embitel.com.chartengine.renderer.XYSeriesRenderer;
import embitel.com.chartengine.util.Helper;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String DATE_FORMAT = "EEEE";
    RelativeLayout chartContainer;
    //views
    private GraphicalView mChart;
    private LinearLayout mLvDetailsView, mLvDetailsInfo;
    private RelativeLayout mDrawingView;
    private TextView mTvDate, mTvTime, mTvDistance;
    // for drawing line
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mTriangleImage;
    private int mTriangleHeight, mTriangleWidth;
    private Helper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = Helper.getInstance();
        // view initialization
        mLvDetailsView = (LinearLayout) findViewById(R.id.lvDetailsView);
        mLvDetailsInfo = (LinearLayout) findViewById(R.id.lvDetailsInfoLayout);
        mDrawingView = (RelativeLayout) findViewById(R.id.drawingView);
        mTvDate = (TextView) findViewById(R.id.tvDate);
        mTvDistance = (TextView) findViewById(R.id.tvDistance);
        mTvTime = (TextView) findViewById(R.id.tvTime);

        mTriangleImage = BitmapFactory.decodeResource(getResources(), R.drawable.triangle);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(MainActivity.this, R.drawable.triangle);
        mTriangleHeight = bitmapDrawable.getBitmap().getHeight();
        mTriangleWidth = bitmapDrawable.getBitmap().getWidth();

        // get screen width and height
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ffb74d"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 0));
        mCanvas.drawColor(Color.parseColor("#323A47"));
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        setBackgroundDrawable(drawable);
        openChart();
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundDrawable(BitmapDrawable drawable) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            mDrawingView.setBackground(drawable);
        } else {
            mDrawingView.setBackgroundDrawable(drawable);
        }
    }

    private void openChart() {
        Date[] dates = new Date[9];
        for (int i = 0; i < 9; i++) {
            GregorianCalendar gc = new GregorianCalendar(2016, 9, 8 + i);
            dates[i] = gc.getTime();
        }
        int[] views = {2, 4, 6, 5, 8, 7, 6};

        // Creating TimeSeries for Views
        TimeSeries viewsSeries = new TimeSeries("Views");

        // Adding data to Views Series
        for (int i = 0; i < views.length; i++) {

            viewsSeries.add(dates[i + 1], views[i]);
        }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(viewsSeries);  // Adding Visits Series to dataset

        // Creating XYSeriesRenderer to customize viewsSeries
        XYSeriesRenderer viewsRenderer = new XYSeriesRenderer();
        viewsRenderer.setColor(Color.WHITE);
        viewsRenderer.setPointStyle(PointStyle.CIRCLE);
        viewsRenderer.setFillPoints(true);
        viewsRenderer.setLineWidth(6f);
        viewsRenderer.setPointStrokeWidth(5f);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setLabelsTextSize(25f);
        multiRenderer.setShowGridY(true);
        multiRenderer.setPointSize(30f);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setShowLegend(false);
        // We want to avoid black border
        multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Adding viewsRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(viewsRenderer);
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(55);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(10);
        multiRenderer.setXAxisMin(dates[0].getTime());
        multiRenderer.setXAxisMax(dates[8].getTime());
        multiRenderer.setYLabelsPadding(12);
        multiRenderer.setXRoundedLabels(false);
        multiRenderer.setShowGridX(true);
        multiRenderer.setShowTickMarks(false);
        multiRenderer.setXLabels(7);
        multiRenderer.setGridXPathEffect(true);
    //    multiRenderer.addYTextLabel(2, "NOT Taken");
    //    multiRenderer.setShowAxes(false);

        // Getting a reference to LinearLayout of the MainActivity Layout
        chartContainer = (RelativeLayout) findViewById(R.id.chart_container);
        // Creating a Time Chart
        mChart = ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer, DATE_FORMAT);
        // Setting a click event listener for the graph
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPointDetails();
            }
        });
        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(mChart);
    }

    private void getPointDetails() {
        Format formatter = new SimpleDateFormat("dd MMM", Locale.getDefault());
        SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
        if (seriesSelection != null) {
            // Getting the clicked Date ( x value )
            long clickedDateSeconds = (long) seriesSelection.getXValue();
            Date clickedDate = new Date(clickedDateSeconds);
            String strDate = formatter.format(clickedDate);
            // Getting the y value
            int amount = (int) seriesSelection.getValue();
            // update UI
            mLvDetailsInfo.setVisibility(View.GONE);
            mLvDetailsView.setVisibility(View.VISIBLE);
            mTvDate.setText(strDate);
            mTvDistance.setText(amount + " m");
            mTvTime.setText(amount - 2 + " Seconds");
            mHelper.setClickedPointIndex(seriesSelection.getPointIndex()); // save pointIndex to change color
            // get (X,Y) co-ordinate of clicked point
            float clickedPointXValue = mHelper.getPointXValue(seriesSelection.getPointIndex());
            float clickedPointYValue = mHelper.getPointYValue(seriesSelection.getPointIndex());
            // get Y co-ordinate of detail view
            float detailsViewY = mLvDetailsView.getHeight() + mLvDetailsView.getY() + 20;
            Log.d(TAG, "getPointDetails: detailsViewY: " + detailsViewY);

            Log.d(TAG, "clickedPointYValue :" + clickedPointYValue);
//            300 =48
//            200 = 32
//            100 = 16;
//            50 = 8;
            int percentage = (int) (clickedPointYValue * 0.17);

            ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams) mLvDetailsView.getLayoutParams();
            int bottomMargin = vlp.bottomMargin;

            Log.d(TAG, "BottomMargin: " + bottomMargin + " chartY: " + chartContainer.getY());

            float y = chartContainer.getY();
            // draw line
            mCanvas.drawColor(Color.parseColor("#323A47"));
            mHelper.setChart(mChart);

            mCanvas.drawLine(clickedPointXValue, detailsViewY + mTriangleHeight / 2,
                    clickedPointXValue, clickedPointYValue + detailsViewY + percentage, mPaint);

            /*mCanvas.drawLine(clickedPointXValue, y - bottomMargin-mTriangleHeight / 2+mLvDetailsView.getHeight()/2,
                    clickedPointXValue, y+mLvDetailsView.getHeight()/2 , mPaint);*/

            mCanvas.drawBitmap(mTriangleImage, clickedPointXValue - mTriangleWidth / 2, detailsViewY, null);
         /*   mCanvas.drawBitmap(mTriangleImage, clickedPointXValue - mTriangleWidth / 2,
                    y - bottomMargin+mLvDetailsView.getHeight()/2, null);*/
            mChart.invalidate();
            mDrawingView.invalidate();
        }
    }
}
