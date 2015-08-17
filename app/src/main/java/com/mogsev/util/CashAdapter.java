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
 * Created by zhenya on 05.08.2015.
 */
public class CashAdapter extends ArrayAdapter<CurrencyInformer> {
    private static final String TAG = "CashAdapter";
    Context context;
    private LayoutInflater layoutInflater;
    private TextView buyDelta;
    private TextView saleDelta;
    private CurrencyInformer cur;

    public CashAdapter(Context context) {
        super(context, R.layout.item_cash_informer);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Set data
     *
     * @param data
     */
    public void setData(List<CurrencyInformer> data) {
        Log.d(TAG, "setData start");
        this.clear();
        if (data != null) {
            this.addAll(data);
            Log.d(TAG, "setData " + data.toString());
        }
        Log.d(TAG, "setData end");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_cash_informer, parent, false);
        }
        cur = this.getItem(position);
        buyDelta = (TextView) view.findViewById(R.id.tvInformerBuyDelta);
        saleDelta = (TextView) view.findViewById(R.id.tvInformerSaleDelta);
        buyDelta.setText(cur.getBuyDelda());
        saleDelta.setText(cur.getSaleDelta());
        // Set color buy delta
        if (Double.parseDouble(cur.getBuyDelda()) > 0) {
            buyDelta.setTextColor(context.getResources().getColor(R.color.ColorDeltaUp));
        } else if (Double.parseDouble(cur.getBuyDelda()) == 0) {
            buyDelta.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));
        } else {
            buyDelta.setTextColor(context.getResources().getColor(R.color.ColorDeltaDown));
        }
        // Set color sale delta
        if (Double.parseDouble(cur.getSaleDelta()) > 0) {
            saleDelta.setTextColor(context.getResources().getColor(R.color.ColorDeltaUp));
        } else if (Double.parseDouble(cur.getSaleDelta()) == 0) {
            saleDelta.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));
        } else {
            saleDelta.setTextColor(context.getResources().getColor(R.color.ColorDeltaDown));
        }
        ((TextView) view.findViewById(R.id.tvInformerCode)).setText(cur.getCode());
        ((TextView) view.findViewById(R.id.tvInformerName)).setText(cur.getName());
        ((TextView) view.findViewById(R.id.tvInformerBuy)).setText(cur.getBuy());
        ((TextView) view.findViewById(R.id.tvInformerSale)).setText(cur.getSale());
        return view;
    }
}
