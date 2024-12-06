package com.haruma.app.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haruma.app.R;
import com.haruma.app.model.Diary;

import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    private Activity activity;
    private List<Diary> di;


    public DashboardAdapter(Activity activity, List<Diary> di){
        this.activity=activity;
        this.di=di;
    }


    @Override
    public int getCount() {
        return di.toArray().length;
    }

    @Override
    public Object getItem(int position) {
        return di.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater flater=activity.getLayoutInflater();
        convertView=flater.inflate(R.layout.layout, null);
        TextView tv=(TextView)convertView.findViewById(R.id.textView9);
        tv.setText(di.get(position).toString());
        return convertView;
    }
}

