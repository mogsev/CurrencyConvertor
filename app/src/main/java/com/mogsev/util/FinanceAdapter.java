package com.mogsev.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogsev.currencyconvertor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenya on 06.08.2015.
 */
public class FinanceAdapter extends ArrayAdapter<Finance> {
    private static final String TAG = "FinanceAdapter";
    Context context;
    private LayoutInflater layoutInflater;
    private TextView tvCity;
    private TextView tvAddress;
    private Finance finance;
    private List<Finance> financesList = new ArrayList<>();

    public FinanceAdapter(Context context) {
        super(context, R.layout.item_finance_currency);
        this.context = context;
    }

    public FinanceAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Populate new items in the list.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_finance, parent, false);

        finance = this.getItem(position);
        Log.d(TAG, finance.toString());
        ((TextView) view.findViewById(R.id.tvBankTitle)).setText(finance.getTitle());
        tvCity = (TextView) view.findViewById(R.id.tvBankCity);
        tvCity.setText(R.string.short_city);
        tvCity.append(" ");
        tvCity.append(finance.getCity());
        tvCity.append("\t");
        tvCity.append(finance.getRegion());
        tvAddress = (TextView) view.findViewById(R.id.tvBankAddress);
        tvAddress.setText(finance.getAddress());
        tvAddress.append("\t");
        tvAddress.append("Tel.");
        tvAddress.append(" ");
        tvAddress.append(finance.getPhone());

        LinearLayout llCurrency = (LinearLayout) view.findViewById(R.id.llFinance);
        List list = finance.getCurrencies();
        for (int i = 0; i < list.size(); i++) {
            View viewCur = layoutInflater.inflate(R.layout.item_finance_currency, parent, false);
            CurrencyInformer cur = (CurrencyInformer) list.get(i);
            ((TextView) viewCur.findViewById(R.id.tvCurrencyName)).setText(cur.getCode());
            ((TextView) viewCur.findViewById(R.id.tvCurrencyBuy)).setText(cur.getBuy());
            ((TextView) viewCur.findViewById(R.id.tvCurrencySale)).setText(cur.getSale());
            llCurrency.addView(viewCur);
        }
        return view;
    }

    /**
     * Set the new data in adapter
     *
     * @param data
     */
    public void setData(final List<Finance> data) {
        Log.d(TAG, "setData start");
        this.clear();
        if (data != null) {
            financesList = data;
            this.addAll(data);
            Log.d(TAG, " " + data.size());
            Log.d(TAG, " " + data.toString());
            Log.d(TAG, "setData end");
        }
    }

    /**
     * Find city and change data in adapter
     *
     * @param city
     */
    public void notifyCity(String city) {
        Log.d(TAG, "notifyCity start");
        this.clear();
        for (Finance finance : financesList) {
            if (finance.getCity().equals(city)) {
                this.add(finance);
            }
        }
        Log.d(TAG, "notifyCity end");
    }
}
