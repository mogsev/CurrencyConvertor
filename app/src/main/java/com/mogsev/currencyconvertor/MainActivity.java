package com.mogsev.currencyconvertor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private final static String urlWebService = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?";
    private final static String conversionRateResult = "ConversionRateResult";
    private final static String conversionRateException = "ConversionRateException";
    private TextView value;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private ArrayAdapter<CharSequence> adapterCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFromCurrency = (Spinner) this.findViewById(R.id.from_currency);
        spinnerToCurrency = (Spinner) this.findViewById(R.id.to_currency);

         adapterCurrency = ArrayAdapter.createFromResource(this,
                R.array.currency, android.R.layout.simple_spinner_item );
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapterCurrency);
        spinnerToCurrency.setAdapter(adapterCurrency);

        value = (TextView) this.findViewById(R.id.value);
        value.setText("1234567890");
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
                        //handler.sendEmptyMessage(0);
                }
            });
        thread.start();
        Log.d("getRate", "End getRate");
    }

    /**
     * Example result - "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=UAH"
     * @return
     */
    private String getUrl() {
        //"http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=UAH"
        StringBuilder url = new StringBuilder();
        url.append(urlWebService);
        // get position spinners
        int numFromCurrency = spinnerFromCurrency.getSelectedItemPosition();
        int numToCurrency = spinnerToCurrency.getSelectedItemPosition();
        // get string result spinners
        String from = adapterCurrency.getItem(numFromCurrency).toString();
        String to = adapterCurrency.getItem(numToCurrency).toString();

        url.append("FromCurrency=").append(from);
        url.append("&ToCurrency=").append(to);
        Log.d("getUrl", url.toString());
        return url.toString();
    }

    /**
     *
     * @param view
     */
    public void getRate(View view) {
        ConversionRate conversionRate = new ConversionRate(); // Создаем экземпляр
        conversionRate.execute(getUrl()); // запускаем
    }

    /**
     *
     */
    private class ConversionRate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                URL urlConversion = new URL(params[0]);
                URLConnection connection = urlConversion.openConnection();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(connection.getInputStream());
                result = doc.getDocumentElement().getTextContent();
                Log.d(conversionRateResult, result);
            } catch (Exception ex) {
                Log.d(conversionRateException, ex.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            value.setText(String.valueOf(s));
        }
    }
}
