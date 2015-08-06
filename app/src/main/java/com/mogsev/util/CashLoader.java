package com.mogsev.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.mogsev.util.CurrencyInformer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
    private static final String URL_NBRB = "http://www.nbrb.by/Services/XmlExRatesRef.aspx";
    private static final String TAG = "CashLoader";
    private static final String TAG_CURRENCY = "Currency";
    private static final String TAG_ELEMENT = "Element";
    private HashMap<String, String> hashName = new HashMap<>();
    private HashMap<String, String> hashNameEnglish = new HashMap<>();
    protected Document documentCash;
    private List<CurrencyInformer> listCash;

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
        initHash();
        initCash();
        Log.d(TAG, "loadInBackground stop");
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

    /**
     * Initialization data of currency
     */
    private void initCash() {
        Log.d(TAG, "Start initCash");
        listCash = new ArrayList<>();
        try {
            URL url = new URL(URL_CASH);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(connection.getInputStream());
            NodeList listElement = document.getElementsByTagName(TAG_ELEMENT);
            Node node, code, buy, sale, buyDelta, saleDelta = null;
            NodeList nodeList = null;
            for (int i = 0; i < listElement.getLength() - 1; i++) {
                node = listElement.item(i);
                nodeList = node.getChildNodes();
                code = nodeList.item(0);
                buy = nodeList.item(1);
                sale = nodeList.item(2);
                buyDelta = nodeList.item(3);
                saleDelta = nodeList.item(4);
                CurrencyInformer currencyInformer = new CurrencyInformer.Builder(code.getTextContent())
                        .name(hashName.get(code.getTextContent()))
                        .buy(buy.getTextContent())
                        .sale(sale.getTextContent())
                        .buyDelta(buyDelta.getTextContent())
                        .saleDelta(saleDelta.getTextContent()).build();
                listCash.add(currencyInformer);
                Log.d(TAG, "Value currencyInformer: " + currencyInformer.toString());
            }
        } catch (Exception ex) {
            Log.d(TAG, "Catch initCash: " + ex.toString());
        }
    }

    /**
     * Initialization name of currency
     */
    private void initHash() {
        Log.d(TAG, "initHash start");
        try {
            URL url = new URL(URL_NBRB);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(connection.getInputStream());

            NodeList nodeList = doc.getElementsByTagName(TAG_CURRENCY);
            Node node, charCode, name, nameEnglish = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);
                NodeList childNodes = node.getChildNodes();
                charCode = childNodes.item(3);
                name = childNodes.item(7);
                nameEnglish = childNodes.item(9);
                hashName.put(charCode.getTextContent(), name.getTextContent());
                hashNameEnglish.put(charCode.getTextContent(), nameEnglish.getTextContent());
            }
        } catch (Exception ex) {
            Log.d(TAG_CURRENCY, ex.toString());
        }
        Log.d(TAG, "initHash stop");
    }

}
