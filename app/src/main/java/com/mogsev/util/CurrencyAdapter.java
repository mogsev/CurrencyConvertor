package com.mogsev.util;

import android.content.Context;
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
    private ArrayList list;
    private CurrencyModel currencyModel;
    private LayoutInflater layoutInflater;

    public CurrencyAdapter( MainActivity activity, ArrayList list) {
        super(activity, android.R.layout.simple_spinner_item, list);
        this.list = list;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        currencyModel = (CurrencyModel) list.get(position);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        textView.setText(currencyModel.getCode());
        textView.append(" - ");
        textView.append(currencyModel.getName());

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = layoutInflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        currencyModel = (CurrencyModel) list.get(position);
        TextView textView = (TextView) row.findViewById(android.R.id.text1);
        textView.setText(currencyModel.getCode());

        return row;
    }

    public ArrayList getList() {
        return list;
    }

    public String getCode(int position) {
        CurrencyModel currencyModel = (CurrencyModel) list.get(position);

        return currencyModel.getCode();
    }

    public void refreshCurrencyAdapter(Currency currency) {
        for (int i = 0; i < this.getCount(); i++) {
            CurrencyModel currencyModel = (CurrencyModel) this.getList().get(i);
            currencyModel.setName(currency.getName(currencyModel.getCode()));
        }
        this.setNotifyOnChange(true);
    }
}
