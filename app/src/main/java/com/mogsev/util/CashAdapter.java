package com.mogsev.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mogsev.currencyconvertor.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhenya on 05.08.2015.
 */
public class CashAdapter extends ArrayAdapter<CurrencyInformer> {
    private static final String URL_CASH = "http://cashexchange.com.ua/XmlApi.ashx";
    private static final String TAG = "CashAdapter";
    protected Document documentCash;
    private List<CurrencyInformer> listCash = new ArrayList<>();
    Context context;
    private LayoutInflater layoutInflater;
    private HashMap<String, String> hashName;
    private Currency currency = Currency.getInstance();

    public CashAdapter(Context context) {
        super(context, R.layout.item_cash_informer);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CashAdapter(Context context, List list) {
        super(context, R.layout.item_cash_informer);
        this.context = context;
        listCash = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CurrencyInformer> data) {
        Log.d(TAG, "setData start");
        this.clear();
        if (data != null) {
            this.addAll(data);
        }
        this.notifyDataSetChanged();
        Log.d(TAG, "setData " + data.toString());
    }

    private void xmlUpdateCash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL_CASH);
                    URLConnection connection = url.openConnection();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    documentCash = db.parse(connection.getInputStream());
                    updateListCash();
                    updateListName();

                } catch (Exception ex) {
                    Log.d("xmlUpdateCash", ex.toString());
                }
            }
        }).start();
    }

    /**
     *
     * @return
     */
    public void updateListCash() {
        listCash.clear();
        NodeList listCurrency = documentCash.getElementsByTagName("Currency");
        NodeList listBuy = documentCash.getElementsByTagName("Buy");
        NodeList listSale = documentCash.getElementsByTagName("Sale");
        NodeList listBuyDelta = documentCash.getElementsByTagName("BuyDelta");
        NodeList listSaleDelta = documentCash.getElementsByTagName("SaleDelta");
        for (int i = 0; i < listCurrency.getLength() - 1; i++) {
            CurrencyInformer currencyInformer = new CurrencyInformer.Builder(listCurrency.item(i).getTextContent())
                    .buy(listBuy.item(i).getTextContent())
                    .sale(listSale.item(i).getTextContent())
                    .buyDelta(listBuyDelta.item(i).getTextContent())
                    .saleDelta(listSaleDelta.item(i).getTextContent()).build();
            listCash.add(currencyInformer);
            this.notifyDataSetChanged();
            Log.d("updateListCash", currencyInformer.toString());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_cash_informer, parent, false);
        }
        CurrencyInformer cur = this.getItem(position);
        //CurrencyInformer cur = listCash.get(position);
        ((TextView) view.findViewById(R.id.tvInformerCode)).setText(cur.getCode());
        ((TextView) view.findViewById(R.id.tvInformerName)).setText(cur.getName());
        ((TextView) view.findViewById(R.id.tvInformerBuy)).setText(cur.getBuy());
        ((TextView) view.findViewById(R.id.tvInformerBuyDelta)).setText(cur.getBuyDelda());
        ((TextView) view.findViewById(R.id.tvInformerSale)).setText(cur.getSale());
        ((TextView) view.findViewById(R.id.tvInformerSaleDelta)).setText(cur.getSaleDelta());
        return view;
    }

    private void updateListName() {
        for (CurrencyInformer cur : listCash) {
            cur.setName(currency.getHashName().get(cur.getCode()));
            this.notifyDataSetChanged();
            Log.d("updateListName", cur.toString() + " " + currency.getName(cur.getCode()));
        }
    }




}
