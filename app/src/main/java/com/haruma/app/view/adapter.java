package com.haruma.app.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haruma.app.R;
import com.haruma.app.model.Diary;

import java.util.ArrayList;

public class adapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Diary> di;


    public adapter(Activity activity, ArrayList<Diary> di){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater flater=activity.getLayoutInflater();
        convertView=flater.inflate(R.layout.layout, null);
        TextView tv=(TextView)convertView.findViewById(R.id.textView9);
        tv.setText(di.get(position).toString());
        return convertView;
    }
}

