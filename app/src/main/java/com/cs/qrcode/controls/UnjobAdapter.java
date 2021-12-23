package com.cs.qrcode.controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs.qrcode.R;

import java.util.ArrayList;

public class UnjobAdapter extends ArrayAdapter<Unjob> {

    Context context;
    private ArrayList<Unjob> unjobs;

    public UnjobAdapter(Context context, int textViewResourceId, ArrayList<Unjob> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.unjobs = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_uncheck, null);
        }
        Unjob o = unjobs.get(position);
        if (o != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView c_id = (TextView) v.findViewById(R.id.c_id);
            TextView unit = (TextView) v.findViewById(R.id.unit);
            TextView department = (TextView) v.findViewById(R.id.department);
            name.setText(String.valueOf(o.getName()));
            c_id.setText(String.valueOf(o.getC_id()));
            unit.setText(String.valueOf(o.getUnit()));
            department.setText(String.valueOf(o.getDepartment()));
        }
        return v;
    }
}
