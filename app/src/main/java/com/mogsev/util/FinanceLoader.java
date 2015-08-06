package com.mogsev.util;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
 * Created by zhenya on 06.08.2015.
 */
public class FinanceLoader extends AsyncTaskLoader<List<Finance>> {
    private static final String TAG = "FinanceLoader";
    private static final String URL_FINANCE = "http://resources.finance.ua/ru/public/currency-cash.xml";
    private static final String NL_CITIES = "cities";
    private static final String NL_REGIONS = "regions";
    private static final String NL_ORGANIZATIONS = "organizations";

    private HashMap<String, String> hashCities = new HashMap<>();
    private HashMap<String, String> hashRegions = new HashMap<>();
    private List<Finance> listFinance;

    public FinanceLoader(Context context) {
        super(context);
    }

    @Override
    public List<Finance> loadInBackground() {
        Log.d(TAG, "loadInBackground start");
        initData();
        Log.d(TAG, "loadInBackground end");
        return listFinance;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (listFinance != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(listFinance);
        }

        if (takeContentChanged() || listFinance == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    private void initData() {
        Log.d(TAG, "initData start");
        listFinance = new ArrayList<>();
        try {
            Log.d(TAG, "Begin connection");
            URL url = new URL(URL_FINANCE);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(connection.getInputStream());
            NodeList cities = document.getElementsByTagName(NL_CITIES).item(0).getChildNodes();
            NodeList regions = document.getElementsByTagName(NL_REGIONS).item(0).getChildNodes();
            NodeList organizations = document.getElementsByTagName(NL_ORGANIZATIONS).item(0).getChildNodes();
            Log.d(TAG, "Begin handling cities " + cities.getLength());
            for (int i = 0; i < cities.getLength(); i++) {
                Node node = cities.item(i);
                NamedNodeMap map = node.getAttributes();
                Node id = map.getNamedItem("id");
                Node title = map.getNamedItem("title");
                hashCities.put(id.getTextContent(), title.getTextContent());
                Log.d(TAG, "initData " + id.getTextContent() + " " + title.getTextContent());
            }
            for (int i = 0; i < regions.getLength(); i++) {
                Node node = regions.item(i);
                NamedNodeMap map = node.getAttributes();
                Node id = map.getNamedItem("id");
                Node title = map.getNamedItem("title");
                hashRegions.put(id.getTextContent(), title.getTextContent());
            }
            Log.d(TAG, "hashRegions: " + hashRegions.size());
            for (int i = 0; i < organizations.getLength(); i++) {
                NodeList nodeList = organizations.item(i).getChildNodes();
                Node title  = nodeList.item(0).getAttributes().getNamedItem("value");
                Node region = nodeList.item(1).getAttributes().getNamedItem("id");
                Node city = nodeList.item(2).getAttributes().getNamedItem("id");
                Node phone = nodeList.item(3).getAttributes().getNamedItem("value");
                Node address = nodeList.item(4).getAttributes().getNamedItem("value");
                listFinance.add(new Finance.Builder()
                        .title(title.getTextContent())
                        .region(hashRegions.get(region.getTextContent()))
                        .city(hashCities.get(city.getTextContent()))
                        .phone(phone.getTextContent())
                        .address(address.getTextContent())
                        .build());
                NodeList currencies = nodeList.item(6).getChildNodes();
                Log.d(TAG, "currencies length " + currencies.getLength());
                for (int n = 0; n < currencies.getLength(); n++) {
                    Node currency = currencies.item(n);
                    Node code = currency.getAttributes().getNamedItem("id");
                    Node buy = currency.getAttributes().getNamedItem("br");
                    Node sale = currency.getAttributes().getNamedItem("ar");
                    listFinance.get(i).addCurrency(new CurrencyInformer.Builder(code.getTextContent())
                            .buy(buy.getTextContent())
                            .sale(sale.getTextContent())
                            .build());
                }
                Log.d(TAG, listFinance.get(i).toString());
            }
        } catch (Exception ex) {
            Log.d(TAG, "initData " + ex.toString());
        }

        listFinance.add(new Finance.Builder().title("AVAL").city("Житомир").build());
        listFinance.add(new Finance.Builder().title("PRIVAT").city("Киев").build());
        Log.d(TAG, "initData stop" + listFinance.toString());
    }
}
