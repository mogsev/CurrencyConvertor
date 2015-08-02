package com.mogsev.currencyconvertor;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogsev.util.Currency;
import com.mogsev.util.CurrencyAdapter;
import com.mogsev.util.CurrencyURL;
import com.mogsev.util.CurrencyModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Converter extends AppCompatActivity {
    // Commit Log
    private final static String CRR = "ConversionRateResult";
    private final static String CRE = "ConversionRateException";
    private final static String GET_URL = "GetURL";

    private final static String URL_WEB_SERVICE = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?";

    // Initialize View elements
    private TextView value;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private TextView textViewFromCurrency;
    private TextView textViewFromCurrencyName;
    private TextView textViewFromRate;
    private TextView textViewToCurrency;
    private TextView textViewToCurrencyName;
    private TextView textViewToRate;

    private Currency currency;
    private String[] listCode;
    private ArrayList<CurrencyModel> list;
    private CurrencyAdapter currencyAdapter;

    private ProgressDialog progressDialog;

    /**
     * Initialize View elements
     */
    private void initView() {
        value = (TextView) this.findViewById(R.id.value);
        spinnerFromCurrency = (Spinner) this.findViewById(R.id.from_currency);
        spinnerToCurrency = (Spinner) this.findViewById(R.id.to_currency);

        textViewFromCurrency = (TextView) this.findViewById(R.id.textViewFromCurrency);
        textViewFromCurrencyName = (TextView) this.findViewById(R.id.textViewFromCurrencyName);
        textViewFromRate = (TextView) this.findViewById(R.id.textViewFromRate);

        textViewToCurrency = (TextView) this.findViewById(R.id.textViewToCurrency);
        textViewToCurrencyName = (TextView) this.findViewById(R.id.textViewToCurrencyName);
        textViewToRate = (TextView) this.findViewById(R.id.textViewToRate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View elements
        initView();

        // Initialize CurrencyAdapter
        currency = new Currency();
        listCode = getResources().getStringArray(R.array.currency);
        currency.setListCode(listCode);
        list = currency.getListCurrency();

        currencyAdapter = new CurrencyAdapter(this.getApplication(), list);
        spinnerFromCurrency.setAdapter(currencyAdapter);
        spinnerToCurrency.setAdapter(currencyAdapter);

        //***********************************************************
        spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyModel currencyModel = (CurrencyModel) parent.getSelectedItem();
                textViewFromCurrency.setText(currencyModel.getCode());
                textViewFromCurrencyName.setText(currencyModel.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyModel currencyModel = (CurrencyModel) parent.getSelectedItem();
                textViewToCurrency.setText(currencyModel.getCode());
                textViewToCurrencyName.setText(currencyModel.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerFromCurrency.setSelection(61);
        spinnerToCurrency.setSelection(59);
        //***********************************************************
    }

    public void getRateCash(View view) {
        Log.d("getRate", "Begin getRate");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlConversion = new URL("http://cashexchange.com.ua/XmlApi.ashx");
                    URLConnection connection = urlConversion.openConnection();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(connection.getInputStream());

                    NodeList nodeList = doc.getElementsByTagName("Element");

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element element = (Element) nodeList.item(i);

                        Node currency = element.getFirstChild();
                        Log.d("getRate", currency.getTextContent());

                        Node buy = element.getLastChild();
                        Log.d("getRate", buy.getTextContent());

                        Node sale = element.getLastChild();
                        Log.d("getRate", sale.getTextContent());

                        Node buyDelta = element.getLastChild();
                        Log.d("getRate", buyDelta.getTextContent());

                        Node saleDelta = element.getLastChild();
                        Log.d("getRate", saleDelta.getTextContent());
                    }
                } catch (Exception ex) {
                    Log.d("getRate", ex.toString());
                }
                //handler.sendEmptyMessage(0);
            }
        });
        thread.start();
        Log.d("getRate", "End getRate");
    }

    /**
     * Example url - "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=UAH"
     * Example urlInverse - "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=UAH&ToCurrency=USD"
     *
     * @return
     */
    private String getUrl(int i) {
        StringBuilder url = new StringBuilder();

        // Create url
        url.append(URL_WEB_SERVICE);
        // get selected position spinners
        int numFromCurrency = spinnerFromCurrency.getSelectedItemPosition();
        int numToCurrency = spinnerToCurrency.getSelectedItemPosition();
        // get string result spinners
        String from = currencyAdapter.getCode(numFromCurrency);
        String to = currencyAdapter.getCode(numToCurrency);
        //String to = currencyAdapter.getItem(numToCurrency).toString();

        textViewFromCurrency.setText(from);
        textViewToCurrency.setText(to);

        textViewFromCurrencyName.setText(currency.getName(from));
        textViewToCurrencyName.setText(currency.getName(to));

        switch (i) {
            case CurrencyURL.URL_NORMAL:
                url.append(CurrencyURL.FROM).append(from);
                url.append(CurrencyURL.TO).append(to);
                break;
            case CurrencyURL.URL_INVERSE:
                url.append(CurrencyURL.FROM).append(to);
                url.append(CurrencyURL.TO).append(from);
                break;
        }

        Log.d(GET_URL, url.toString());
        return url.toString();
    }

    /**
     * @param view
     */
    public void getRate(View view) {
        ConversionRate conversionRate = new ConversionRate(); // Create
        conversionRate.execute(getUrl(CurrencyURL.URL_NORMAL)); // Start
        ConversionRateInverse conversionRateInverse = new ConversionRateInverse(); // Create
        conversionRateInverse.execute(getUrl(CurrencyURL.URL_INVERSE)); // Start
    }

    /**
     * @param str
     * @return
     */
    private String connection(String str) {
        String result = null;
        try {
            URL url = new URL(str);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(connection.getInputStream());
            result = doc.getDocumentElement().getTextContent();
            Log.d(CRR, result);
        } catch (Exception ex) {
            Log.d(CRE, ex.toString());
        }
        return result;
    }

    /**
     * Check conversion result
     *
     * @param str
     * @return
     */
    private String checkConversionResult(String str) {
        return (str.equals("-1") || str.equals("")) ? getString(R.string.no_data) : str;
    }

    /**
     * Conversion rate
     */
    private class ConversionRate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return connection(params[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            value.setText(String.valueOf(checkConversionResult(str)));
            textViewFromRate.setText(String.valueOf(checkConversionResult(str)));
        }
    }

    /**
     * Conversion rate inverse
     */
    private class ConversionRateInverse extends ConversionRate {
        @Override
        protected String doInBackground(String... params) {
            return connection(params[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            textViewToRate.setText(String.valueOf(checkConversionResult(str)));
        }
    }

    public void refreshList(View view) {
        for (int i = 0; i < currencyAdapter.getCount(); i++) {
            CurrencyModel currencyModel = (CurrencyModel) currencyAdapter.getList().get(i);
            currencyModel.setName(currency.getName(currencyModel.getCode()));
        }
        currencyAdapter.setNotifyOnChange(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshListOfCurrency();

        //***********************************************************

        //***********************************************************
    }

    /**
     * Refresh list of currency
     */
    private void refreshListOfCurrency() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Refresh list of currency...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currencyAdapter.refreshCurrencyAdapter(currency);
                progressDialog.cancel();
            }
        }).start();
    }
}
