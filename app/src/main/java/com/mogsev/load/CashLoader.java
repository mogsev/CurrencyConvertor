package com.mogsev.load;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.LayoutInflater;

import com.mogsev.util.CurrencyInformer;

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
public class CashLoader extends AsyncTaskLoader<List<CurrencyInformer>> {
    private static final String URL_CASH = "http://cashexchange.com.ua/XmlApi.ashx";
    private static final String TAG = "CashLoader";
    protected Document documentCash;
    private List<CurrencyInformer> listCash;
    Context context;
    private LayoutInflater layoutInflater;
    private HashMap<String, String> hashName;

    /**
     * Stores away the application context associated with context. Since Loaders can be used
     * across multiple activities it's dangerous to store the context directly.
     *
     * @param context used to retrieve the application context.
     */
    public CashLoader(Context context) {
        super(context);
    }

    @Override
    public List loadInBackground() {
        Log.d(TAG, "loadInBackground start");
        try {
            URL url = new URL(URL_CASH);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            documentCash = db.parse(connection.getInputStream());
            updateListCash();
        } catch (Exception ex) {
            Log.d("xmlUpdateCash", ex.toString());
        }
        Log.d(TAG, "loadInBackground stop");
        Log.d(TAG, listCash.toString());
        return listCash;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (listCash != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(listCash);
        }

        if (takeContentChanged() || listCash == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<CurrencyInformer> data) {
        super.deliverResult(data);

    }

    public void updateListCash() {
        listCash = new ArrayList<>();
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
            Log.d("updateListCash", currencyInformer.toString());
        }
    }
}
