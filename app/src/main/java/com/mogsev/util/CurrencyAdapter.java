package com.mogsev.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mogsev.currencyconvertor.MainActivity;

import java.util.ArrayList;

/**
 * Created by zhenya on 30.07.2015.
 */
public class CurrencyAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private ArrayList list;
    public Resources res;
    private CurrencyModel tempValues;
    LayoutInflater layoutInflater;
    //private TextView code;
    //private TextView name;

    public CurrencyAdapter( MainActivity activity, ArrayList list, Resources res) {
        super(activity, android.R.layout.simple_spinner_item, list);
        this.activity = activity;
        this.list = list;
        this.res = res;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        tempValues = (CurrencyModel) list.get(position);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        textView.setText(tempValues.getCode());
        textView.append(" - ");
        textView.append(tempValues.getName());

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        tempValues = (CurrencyModel) list.get(position);
        TextView code = (TextView) row.findViewById(android.R.id.text1);
        code.setText(tempValues.getCode());

        return row;
    }
}
