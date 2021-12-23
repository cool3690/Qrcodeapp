package com.cs.qrcode.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.cs.qrcode.R;

import java.util.ArrayList;

public class JobAdapter extends ArrayAdapter<Job> {

    Context context;
    private ArrayList<Job> jobs;

    public JobAdapter(Context context, int textViewResourceId, ArrayList<Job> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.jobs = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listenlist, null);
        }
        Job o = jobs.get(position);
        if (o != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView c_id = (TextView) v.findViewById(R.id.c_id);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView department = (TextView) v.findViewById(R.id.department);
            name.setText(String.valueOf(o.getName()));
            c_id.setText(String.valueOf(o.getC_id()));
            date.setText(String.valueOf(o.getDate()));
            department.setText(String.valueOf(o.getDepartment()));
        }
        return v;
    }
}
