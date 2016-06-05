package com.example.strikkeapp.app.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.strikkeapp.app.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> patterns;
    private static LayoutInflater inflater = null;

    public CustomAdapter (Context context, ArrayList<String> patterns) {
        this.context = context;
        this.patterns = patterns;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return patterns.size();
    }

    @Override
    public Object getItem(int position) {
        return patterns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.instruction, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(patterns.get(position));
        return vi;
    }
}