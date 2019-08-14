package com.mlz.shuiguang.util.widget.chart;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

/**
 * Created by dehualai on 1/16/17.
 */

public abstract class ChartItem {

    protected static final int TYPE_BARCHART = 0;
    protected static final int TYPE_LINECHART = 1;
    protected static final int TYPE_PIECHART = 2;

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] xAxisLabel;

    public void setxAxisLabel(String[] label){
        xAxisLabel = label;
    }

    protected ChartData<?> mChartData;

    public ChartItem(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context c);
}