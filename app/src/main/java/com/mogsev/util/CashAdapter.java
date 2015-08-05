package com.mogsev.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mogsev.currencyconvertor.R;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhenya on 05.08.2015.
 */
public class CashAdapter extends BaseAdapter {
    private static final String URL_CASH = "http://cashexchange.com.ua/XmlApi.ashx";
    protected Document documentCash;
    private ArrayList<CurrencyInformer> listCash;
    Context context;
    LayoutInflater layoutInflater;
    private HashMap<String, String> hashName;
    private Currency currency = Currency.getInstance();

    public CashAdapter(Context context, ArrayList list) {
        this.context = context;
        listCash = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        xmlUpdateCash();
    }

    private void xmlUpdateCash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    java.net.URL url = new URL(URL_CASH);
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
    public int getCount() {
        return listCash.size();
    }

    @Override
    public Object getItem(int position) {
        return listCash.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_cash_informer, parent, false);
        }

        CurrencyInformer cur = (CurrencyInformer) listCash.get(position);
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
