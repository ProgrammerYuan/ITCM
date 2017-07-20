package com.ntucap.itcm.utils.ChartUtil;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by ProgrammerYuan on 20/07/17.
 */

public class DateAxisValueFormatter implements IAxisValueFormatter {

    private String[] mMonths = new String[]{
            "JAN", "FEB", "MAR",
            "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP",
            "OCT", "NOV", "DEC"
    };

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int date = (int) value;
        int day = date % 100;
        int month = date / 100;
        return mMonths[month] + " " + String.valueOf(day);
    }
}
