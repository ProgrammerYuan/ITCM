package com.ntucap.itcm.utils.ChartUtil;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by ProgrammerYuan on 20/07/17.
 */

public class TemperatureAxisValueFormatter implements IAxisValueFormatter {

    private static final String TEMP_TEMPLATE = "%d ℃";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.format(TEMP_TEMPLATE, (int) value);
    }
}
