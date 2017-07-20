package com.ntucap.itcm.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.ntucap.itcm.R;
import com.ntucap.itcm.utils.ChartUtil.DateAxisValueFormatter;
import com.ntucap.itcm.utils.ChartUtil.TemperatureAxisValueFormatter;
import com.ntucap.itcm.views.IrisSwitchButton;

import java.util.ArrayList;

/**
 * Created by ProgrammerYuan on 19/07/17.
 */

public class DataDetailActivity extends AppCompatActivity implements IrisSwitchButton.OnIrisSwitchListener {

    private LineChart mLineChart;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private IrisSwitchButton mSwDMY;
    private LinearLayout mChartContainer;


    private int COLOR_WHITE ;
    private int COLOR_MAIN_THEME;
    private int COLOR_SECOND_THEME;
    private int COLOR_THIRD_THEME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        mLineChart = (LineChart) findViewById(R.id.daily_line_chart_act_data_detail);
        mBarChart = (BarChart) findViewById(R.id.monthly_bar_chart_act_data_detail);
        mPieChart = (PieChart) findViewById(R.id.yearly_pie_chart_act_data_detail);
        mSwDMY = (IrisSwitchButton) findViewById(R.id.sw_dmy_act_data_detail);
        mSwDMY.setItemsWithArray(new String[]{"DAILY", "MONTHLY", "YEARLY"});
        mSwDMY.setOnSwitchListener(this);
        mChartContainer = (LinearLayout) findViewById(R.id.ll_chart_act_data_detail);
        Resources res = getResources();
        COLOR_WHITE = res.getColor(R.color.white);
        COLOR_MAIN_THEME = res.getColor(R.color.mainTheme);
        COLOR_SECOND_THEME = res.getColor(R.color.secondTheme);
        COLOR_THIRD_THEME = res.getColor(R.color.thirdTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        randomInit();
        initBarChart();
        initPieChart();

    }

    private void initLineChart() {
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.getAxisLeft().setAxisMinimum(0f);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getData().setHighlightEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setDescription(null);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setDrawGridBackground(false);
    }

    private void initBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < 12; i ++) {
            barEntries.add(new BarEntry(i, (float)(40f + 20f * Math.random())));
        }
        BarDataSet dataSet = new BarDataSet(barEntries, "test");
        dataSet.setColor(COLOR_MAIN_THEME);
        BarData barData = new BarData(dataSet);
        mBarChart.setData(barData);
        mBarChart.setDescription(null);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.invalidate();
    }

    private void initPieChart() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i < 3; i ++) {
            pieEntries.add(new PieEntry(1f / 3f + (i - 1) * 0.1f));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "test");
        dataSet.setColors(new int[]{COLOR_MAIN_THEME, COLOR_SECOND_THEME, COLOR_THIRD_THEME});
        dataSet.setValueTextColor(COLOR_WHITE);
        PieData pieData = new PieData(dataSet);
        mPieChart.setData(pieData);
        mPieChart.setDrawCenterText(false);
        mPieChart.setHoleColor(COLOR_WHITE);
        mPieChart.setDescription(null);

        Legend legend = mPieChart.getLegend();
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        legendEntries.add(new LegendEntry("First", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, COLOR_MAIN_THEME));
        legendEntries.add(new LegendEntry("Second", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, COLOR_SECOND_THEME));
        legendEntries.add(new LegendEntry("Third", Legend.LegendForm.DEFAULT, Float.NaN, Float.NaN, null, COLOR_THIRD_THEME));
        legend.setCustom(legendEntries);
        mPieChart.setHoleRadius(50f);
        mPieChart.setTransparentCircleRadius(50f);
        mPieChart.setDrawHoleEnabled(true);
//        mPieChart.setTransparentCircleRadius();
        mPieChart.setDrawSlicesUnderHole(false);
        mPieChart.setEntryLabelColor(getResources().getColor(R.color.white));
        mPieChart.invalidate();
    }

    private void randomInit() {
        ArrayList<Entry> dataList = new ArrayList<>();
        for(int i = 0; i < 7; i ++) {
            dataList.add(new Entry(i, (float)(Math.random() * 20) + 20f));
        }
        LineDataSet dataSet = new LineDataSet(dataList, "Test");
        dataSet.setDrawValues(false);
        dataSet.setColor(COLOR_WHITE);
        dataSet.setCircleColor(COLOR_WHITE);
        dataSet.setCircleColorHole(COLOR_MAIN_THEME);
//        dataSet.setCircleColorHole(getResources().getColor(R.color.trans));
        LineData data = new LineData(dataSet);
        data.setHighlightEnabled(false);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setAxisLineColor(COLOR_WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceMin(20f);
        xAxis.setTextColor(COLOR_WHITE);
        xAxis.setValueFormatter(new DateAxisValueFormatter());
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        YAxis rightYAxis = mLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawGridLines(false);
        YAxis leftYAxis = mLineChart.getAxisLeft();
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextColor(COLOR_WHITE);
        leftYAxis.setAxisLineColor(COLOR_WHITE);
        leftYAxis.setValueFormatter(new TemperatureAxisValueFormatter());

        mLineChart.setData(data);
        mLineChart.setDescription(null);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
//        mLineChart.setBackgroundColor(COLOR_MAIN_THEME);
        mLineChart.invalidate();
    }

    @Override
    public void onSwitch(int switchIndex) {
        switch(switchIndex) {
            case 0:
                mLineChart.setVisibility(View.VISIBLE);
                mBarChart.setVisibility(View.INVISIBLE);
                mPieChart.setVisibility(View.INVISIBLE);
                mChartContainer.setBackgroundColor(COLOR_MAIN_THEME);
                break;
            case 1:
                mLineChart.setVisibility(View.INVISIBLE);
                mBarChart.setVisibility(View.VISIBLE);
                mPieChart.setVisibility(View.INVISIBLE);
                mChartContainer.setBackgroundColor(COLOR_WHITE);
                break;
            case 2:
                mLineChart.setVisibility(View.INVISIBLE);
                mBarChart.setVisibility(View.INVISIBLE);
                mPieChart.setVisibility(View.VISIBLE);
                mChartContainer.setBackgroundColor(COLOR_WHITE);
                break;
        }
    }
}
