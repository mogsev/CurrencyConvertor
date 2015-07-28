package com.mogsev.currencyconvertor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {
    public TextView value;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerFromCurrency = (Spinner) this.findViewById(R.id.from_currency);
        Spinner spinnerToCurrency = (Spinner) this.findViewById(R.id.to_currency);

        ArrayAdapter<CharSequence> adapterFromCurrency = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item );
        adapterFromCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapterFromCurrency);
        spinnerToCurrency.setAdapter(adapterFromCurrency);

        final TextView value = (TextView) this.findViewById(R.id.value);
        value.setText("1234567890");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    value.setText("00000");
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                            Node currency =  element.getFirstChild();
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
                        handler.sendEmptyMessage(0);
                }
            });
        thread.start();
        Log.d("getRate", "End getRate");
    }

    public void getRate(View view) {

        ConversionRate conversionRate = new ConversionRate(); // Создаем экземпляр
        conversionRate.execute(); // запускаем
        /**
        Log.d("getRate", "Begin getRate");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlConversion = new URL("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=UAH");
                    URLConnection connection = urlConversion.openConnection();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(connection.getInputStream());
                    Log.d("getRate", doc.getDocumentElement().getTextContent());
                } catch (Exception ex) {
                    Log.d("getRate", ex.toString());
                }
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
        Log.d("getRate", "End getRate");

         */
    }

    class ConversionRate extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            try {
                URL urlConversion = new URL("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=UAH");
                URLConnection connection = urlConversion.openConnection();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(connection.getInputStream());
                result = doc.getDocumentElement().getTextContent();
                Log.d("getRate", result);
            } catch (Exception ex) {
                Log.d("getRate", ex.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            value.setText(String.valueOf(s));
        }
    }


}
