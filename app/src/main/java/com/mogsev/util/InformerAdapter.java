package com.mogsev.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhenya on 04.08.2015.
 */
public class InformerAdapter {
    private static final String URL_FINANCE = "http://resources.finance.ua/ru/public/currency-cash.xml";

    private HashMap<String, String> hmCities;
    private HashMap<String, String> hmRegions;


    protected Document documentFinance;

    protected boolean isDocument;

    public InformerAdapter() {
        xmlUpdateFinance();
    }

    private void xmlUpdateFinance() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isDocument = false;
                    java.net.URL url = new URL(URL_FINANCE);
                    URLConnection connection = url.openConnection();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    documentFinance = db.parse(connection.getInputStream());
                    isDocument = true;
                    updateCities();
                } catch (Exception ex) {
                    Log.d("xmlUpdate", ex.toString());
                }
            }
        }).start();
    }

    /**
     * Update HashMap cities
     */
    private void updateCities() {
        NodeList ndCities = documentFinance.getElementsByTagName("cities");
        NodeList list = ndCities.item(0).getChildNodes();
        for (int i = 0; i < list.getLength() - 1; i++) {
            Node node = list.item(i);
            Attr id = (Attr) node.getAttributes().getNamedItem("id");
            Attr title = (Attr) node.getAttributes().getNamedItem("title");
            hmCities.put(title.getValue(), id.getValue());
            Log.d("UpdateCities", i + " " + id.getValue() + " " + title.getValue());
        }
    }

    public void refreshAdapter() {
        updateCities();
    }


}
