package com.mogsev.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mogsev.currencyconvertor.R;

import java.util.List;

/**
 * Created by zhenya on 06.08.2015.
 */
public class FinanceAdapter extends ArrayAdapter<Finance>{
    private static final String TAG = "FinanceAdapter";
    Context context;
    private LayoutInflater layoutInflater;
    private TextView title;
    private TextView city;
    private Finance finance;

    public FinanceAdapter(Context context) {
        super(context, R.layout.item_finance);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FinanceAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setData(List<Finance> data) {
        Log.d(TAG, "setData start");
        this.clear();
        if (data != null) {
            this.addAll(data);
        }
        this.notifyDataSetChanged();
        Log.d(TAG, "setData " + data.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_finance, parent, false);
        }
        finance = this.getItem(position);
        ((TextView) view.findViewById(R.id.tvFinanceTitle)).setText(finance.getTitle());
        ((TextView) view.findViewById(R.id.tvFinanceCity)).setText(finance.getCity());
        return view;
    }
}
