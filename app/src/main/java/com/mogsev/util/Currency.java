package com.mogsev.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhenya on 29.07.2015.
 */
public class Currency {
    private static final String URL = "http://www.nbrb.by/Services/XmlExRatesRef.aspx";
    private static final String TAG_NAME = "Currency";

    private HashMap<String, String> hashName;
    private HashMap<String, String> hashNameEnglish;
    private String[] listCode;
    private ArrayList<CurrencyModel> listCurrency;

    /**
     * Main constructor
     */
    public Currency() {
        hashName = new HashMap<>();
        hashNameEnglish = new HashMap<>();
        listCurrency = new ArrayList<CurrencyModel>();
        initHash();
    }

    /**
     * Initialization
     */
    private void initHash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL);
                    URLConnection connection = url.openConnection();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(connection.getInputStream());

                    NodeList nodeList = doc.getElementsByTagName(TAG_NAME);
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
                    Log.d(TAG_NAME, ex.toString());
                }
            }
        }).start();
    }

    /**
     *
     * @param charCode
     * @return
     */
    public String getName(String charCode) {
        return hashName.get(charCode);
    }

    /**
     *
     * @param charCode
     * @return
     */
    public String getNameEnglish(String charCode) {
        return hashNameEnglish.get(charCode);
    }

    /**
     *
     * @param listCode
     */
    public void setListCode(String[] listCode) {
        this.listCode = listCode;
        createListCurrency();
    }

    /**
     *
     * @return
     */
    public ArrayList<CurrencyModel> getListCurrency() {
        return listCurrency;
    }

    /**
     *
     */
    private void createListCurrency() {
        String code = "";
        for (int i = 0; i < listCode.length; i++) {
            code = listCode[i];
            listCurrency.add(new CurrencyModel(code));
        }
    }

    //*************************** Dialog start **************************************



    //************************** End Dialog *****************************************
}
