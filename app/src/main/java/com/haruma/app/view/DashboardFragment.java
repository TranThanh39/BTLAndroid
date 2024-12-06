package com.haruma.app.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.haruma.app.R;
import com.haruma.app.adapter.DashboardAdapter;
import com.haruma.app.dto.DatabaseHelper;
import com.haruma.app.model.Diary;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class DashboardFragment extends Fragment {

    public View root;
    public DatabaseHelper cursor;
    public AppCompatActivity activity;
    public ArrayList<Diary> di;
    public TextView tm;
    public TextView dh;
    public TextView ch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }


    public void show_chart(float num1, float num2){
        PieChart pieChart = root.findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(num1*100f, "Done"));
        pieEntries.add(new PieEntry(num2*100f, "Not Done"));


        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);


        pieChart.invalidate();

        pieChart.setHoleRadius(25f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(android.graphics.Color.WHITE);
        pieChart.setTransparentCircleRadius(20f);
        pieDataSet.setValueTextSize(18f);

    }

    public static boolean isInCurrentWeek(Date dateToCheck) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startOfWeek = calendar.getTime();

        calendar.add(Calendar.DATE, 6);
        Date endOfWeek = calendar.getTime();

        return dateToCheck.equals(startOfWeek) || dateToCheck.equals(endOfWeek)
                || (dateToCheck.after(startOfWeek) && dateToCheck.before(endOfWeek));
    }

    public static boolean isInCurrentMonth(Date checkDate) {
        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.setTime(checkDate);
        int checkMonth = calendar.get(Calendar.MONTH);
        int checkYear = calendar.get(Calendar.YEAR);

        return (currentMonth == checkMonth && currentYear == checkYear);
    }

    @SuppressLint("SetTextI18n")
    public void showListDiary(int mode){
        List<Diary> di2 = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (mode==0){
            Date current = new Date();

            String date = sdf.format(current);

            for (int i=0; i<di.size(); i++){
                if (di.get(i).getDay().equals(date)){
                    di2.add(di.get(i));
                }
            }
        }
        else if (mode==1){
            for (int i=0; i<di.size(); i++){
                try {
                    if (isInCurrentWeek(Objects.requireNonNull(sdf.parse(di.get(i).getDay())))){
                        di2.add(di.get(i));
                    }
                }
                catch (Exception e){
                    return;
                }
            }
        }
        else if (mode==2){
            for (int i=0; i<di.size(); i++){
                try {
                    if (isInCurrentMonth(sdf.parse(di.get(i).getDay()))){
                        di2.add(di.get(i));
                    }
                }
                catch (Exception e){
                    return;
                }
            }
        }
        else{
            di2=di;
        }

        DashboardAdapter adapt = new DashboardAdapter(activity, di2);

        ListView lv = root.findViewById(R.id.listview);
        lv.setAdapter(adapt);
        float done=0;
        for (int i=0; i<di2.size(); i++){
            if (di2.get(i).getStatus()){
                done+=1;
            }
        }
        dh.setText("Số việc đã hoàn thành: "+String.valueOf((int)done));
        ch.setText("Số việc chưa hoàn thành: "+String.valueOf(di2.size()-(int)done));
        float tmp = done/di2.size();
        show_chart(tmp, 1-tmp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = (AppCompatActivity) requireActivity();
        cursor = new DatabaseHelper(activity);
        di= new ArrayList<>(cursor.getAllDiaries());
        tm=root.findViewById(R.id.textMode);
        dh=root.findViewById(R.id.dahoanthanh);
        ch=root.findViewById(R.id.chuahoanthanh);
        showListDiary(0);
        return root;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_analyst, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.it1){
            showListDiary(0);
            tm.setText("Theo ngày");
        }
        if (item.getItemId() == R.id.it2){
            showListDiary(1);
            tm.setText("Theo tuần");
        }
        if (item.getItemId() == R.id.it3){
            showListDiary(2);
            tm.setText("Theo tháng");
        }
        return super.onOptionsItemSelected(item);
    }
}
