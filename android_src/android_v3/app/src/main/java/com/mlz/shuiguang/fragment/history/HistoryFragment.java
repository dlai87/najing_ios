package com.mlz.shuiguang.fragment.history;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.HistoryModelOperation;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.transmission.TransmissionUtil;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.mlz.shuiguang.util.widget.chart.BarChartItem;
import com.mlz.shuiguang.util.widget.chart.ChartItem;
import com.mlz.shuiguang.util.widget.chart.LineChartItem;
import com.mlz.shuiguang.util.widget.chart.PieChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dehualai on 12/24/16.
 */

public class HistoryFragment extends Fragment {

    String TAG = "HistoryFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    View view;
    LinearLayout bgLayout;
    LinearLayout headerLayout;
    LinearLayout noDataLayout;
    ListView lv;

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;

        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history, container, false);
        Bundle args = getArguments();


        Utils.init(mContext);

        assignViews();
        addActionToViews();

        return view;
    }


    private void assignViews(){
        bgLayout = (LinearLayout)view.findViewById(R.id.bg_layout);
        headerLayout = (LinearLayout)view.findViewById(R.id.header_layout);
        lv = (ListView) view.findViewById(R.id.listView1);
        noDataLayout = (LinearLayout)view.findViewById(R.id.no_data_layout);
    }

    private void addActionToViews(){

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        LineData faceTimeData = generateDataLine(DetectionData.FACE);
        if (faceTimeData != null) {
            LineChartItem faceTimeLineChart = new LineChartItem(faceTimeData, mContext);
            faceTimeLineChart.setxAxisLabel(HistoryModelOperation.getDateLabels());
            list.add(faceTimeLineChart);
        }

        LineData handTimeData = generateDataLine(DetectionData.HAND);
        if (handTimeData != null){
            LineChartItem handTimeLineChart = new LineChartItem(handTimeData, mContext);
            handTimeLineChart.setxAxisLabel(HistoryModelOperation.getDateLabels());
            list.add(handTimeLineChart);
        }

        LineData eyesTimeData = generateDataLine(DetectionData.EYES);
        if (eyesTimeData!=null){
            LineChartItem eyesTimeLineChart = new LineChartItem(eyesTimeData, mContext);
            eyesTimeLineChart.setxAxisLabel(HistoryModelOperation.getDateLabels());
            list.add(eyesTimeLineChart);
        }


        PieData pieData = generateDataPie();
        if (pieData!=null){
            list.add(new PieChartItem(pieData, mContext));
        }

        if (faceTimeData==null || handTimeData == null || eyesTimeData ==null || pieData ==null){
            noDataLayout.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }else{
            noDataLayout.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }

        ChartDataAdapter cda = new ChartDataAdapter(mContext, list);
        lv.setAdapter(cda);
    }

    private void refreshViews() {
        ThemeColor theme = Util.getActiveTheme();
        bgLayout.setBackgroundColor(theme.getMainColor(mContext));
        headerLayout.setBackgroundColor(theme.getMinorColor(mContext));
    }


    @Override
    public void onResume(){
        super.onResume();
        // Should not Sync Database here. will read and write database at the same time
       // TransmissionUtil.syncAll(mContext);

        refreshViews();

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }


    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }



    private LineData generateDataLine(String key) {

        //ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        ArrayList<Entry> e2 = new ArrayList<Entry>();

        HistoryModelOperation operation = new HistoryModelOperation();
        HashMap<String, Integer[]> hashMap = operation.getDataByDate(DBAdapter.getDatabase(mContext), key);

        if (hashMap==null || hashMap.isEmpty()){
            return null;
        }



        Integer[] preNursing = hashMap.get(DetectionData.PRE_NURSING);
        Integer[] postNursing = hashMap.get(DetectionData.POST_NURSING);
        for (int i = 0 ; i < preNursing.length; i++){
            e1.add(new Entry(i, preNursing[i]));
            e2.add(new Entry(i, postNursing[i]));
        }


        LineDataSet d1 = new LineDataSet(e1, Util.translateBodyPart(mContext, key) + "," + mContext.getResources().getString(R.string.pre_nursing));
        d1.setLineWidth(3f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);


        LineDataSet d2 = new LineDataSet(e2,  Util.translateBodyPart(mContext, key) + "," + mContext.getResources().getString(R.string.post_nursing));
        d2.setLineWidth(3f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);


        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }



    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie() {

        HistoryModelOperation operation = new HistoryModelOperation();
        HashMap<String, Integer> hashMap = operation.getDataByBodyPart(DBAdapter.getDatabase(mContext));


        if (hashMap==null || hashMap.isEmpty()){
            return null;
        }


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry((float) (hashMap.get(DetectionData.HAND)), Util.translateBodyPart(mContext, DetectionData.HAND) ));
        entries.add(new PieEntry((float) (hashMap.get(DetectionData.EYES)), Util.translateBodyPart(mContext,DetectionData.EYES)));
        entries.add(new PieEntry((float) (hashMap.get(DetectionData.FACE)), Util.translateBodyPart(mContext,DetectionData.FACE)));


        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }



///////////////////////////////////////// NOT IN USE
    /**
     *
     * backup , not used yet
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        //d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }
}
