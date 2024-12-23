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
import com.haruma.app.utility.DatabaseHelper;
import com.haruma.app.model.Timetable;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class DashboardFragment extends Fragment {

    public View root;
    public DatabaseHelper cursor;
    public AppCompatActivity activity;
    public List<Timetable> di;
    public TextView tm;
    public TextView dh;
    public TextView ch;
    public PieChart pieChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }


    public void showChart(float num1, float num2){ //num1 là phân trăm các công việc đã hoàn thành
                                                   //num2 là phần trăm công việc chưa hoàn thành

        //Khởi tạo biểu đồ


        //Tạo biểu đồ
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(num1*100, "Thực hiện"));
        pieEntries.add(new PieEntry(num2*100, "Không làm"));

        //Tạo định dạng cho biểu đồ
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        //Tạo và định dạng đối tượng chứa dữ liệu
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        //Hiển thị dữ liệu
        pieChart.setData(pieData);
        pieChart.invalidate();

        //Tùy chỉnh giao diện cho biểu đồ
        pieChart.setHoleRadius(25f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(android.graphics.Color.WHITE);
        pieChart.setTransparentCircleRadius(20f);
        pieDataSet.setValueTextSize(18f);

    }


    public static boolean isInCurrentWeek(Date dateToCheck) {


        //Lấy ngày đầu tiên của tuần
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = calendar.getTime();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date today_date=today.getTime();

        if (today_date.equals(startOfWeek)){
            calendar.add(Calendar.DATE, -7);
            startOfWeek = calendar.getTime();
        }
        //Lấy ngày cuối cùng của tuần
        calendar.add(Calendar.DATE, 1);
        startOfWeek=calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        Date endOfWeek = calendar.getTime();

        //Kiểm tra nếu ngày là ngày đầu hoặc cuối hoặc là trong tuần hay không, nếu có sẽ trả về True
        return dateToCheck.equals(startOfWeek) || dateToCheck.equals(endOfWeek)
                || (dateToCheck.after(startOfWeek) && dateToCheck.before(endOfWeek));
    }



    public static boolean isInCurrentMonth(Date checkDate) {

        //Lấy thông tin tháng và năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        //Lấy ra thông tin tháng và năm của đối tượng Date được truyền vào để kiểm tra
        calendar.setTime(checkDate);
        int checkMonth = calendar.get(Calendar.MONTH);
        int checkYear = calendar.get(Calendar.YEAR);

        //Kiểm tra tháng và năm có trùng với tháng và năm hiện tại không, nếu có sẽ trả về True
        return (currentMonth == checkMonth && currentYear == checkYear);
    }

    @SuppressLint("SetTextI18n")
    public void showListTimetable(int mode){
        List<Timetable> di2 = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (mode==0){

            //Lấy ngày hiện tại
            Date current = new Date();

            //Định dạng lại ngày hiện tại thành chuỗi để so sánh
            String date = sdf.format(current);

            //Kiểm tra các đối tượng có ngày trùng với ngày đã định dạng để thêm vào danh sách
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

        DashboardAdapter adapter = new DashboardAdapter(activity, R.layout.dashboard_list_tile, di2);
        ListView lv = root.findViewById(R.id.listview);
        lv.setAdapter(adapter);
        float done=0;
        for (int i=0; i<di2.size(); i++){
            if (di2.get(i).getStatus()){
                done+=1;
            }
        }
        dh.setText("Số việc thực hiện: "+String.valueOf((int)done));
        ch.setText("Số việc không thực hiện: "+String.valueOf(di2.size()-(int)done));
        if(di2.isEmpty()){
            pieChart.setVisibility(View.INVISIBLE);
            return;
        }
        else{
            pieChart.setVisibility(View.VISIBLE);
        }
        float tmp = done/di2.size();
        showChart(tmp, 1-tmp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activity = (AppCompatActivity) requireActivity();
        cursor = new DatabaseHelper(activity);
        di = cursor.getAllTimeTable();
        tm=root.findViewById(R.id.textMode);
        dh=root.findViewById(R.id.dahoanthanh);
        ch=root.findViewById(R.id.chuahoanthanh);
        pieChart = root.findViewById(R.id.pieChart);
        showListTimetable(0);

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
            showListTimetable(0);
            tm.setText("Theo ngày");
        }
        if (item.getItemId() == R.id.it2){
            showListTimetable(1);
            tm.setText("Theo tuần");
        }
        if (item.getItemId() == R.id.it3){
            showListTimetable(2);
            tm.setText("Theo tháng");
        }
        if (item.getItemId() == R.id.it4){
            showListTimetable(3);
            tm.setText("Tất cả thời gian");
        }
        return super.onOptionsItemSelected(item);
    }
}
