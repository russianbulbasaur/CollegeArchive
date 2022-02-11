package mein.wasser.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import mein.wasser.R;

public class years_adapter extends ArrayAdapter {
private ArrayList<String> years;
private int which  = 0;
    public years_adapter(@NonNull Context context, int resource, ArrayList<String> year,int code) {
        super(context, resource);
        this.years =year;
        this.which = code;
    }

    @Override
    public int getCount() {
        return years.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.year_listview,parent,false);
        MaterialCardView card = v.findViewById(R.id.yearcardview);
        TextView tv = (TextView)v.findViewById(R.id.yearname);
        tv.setTextSize(14f);
        tv.setText(years.get(position));
        return v;
    }
}
