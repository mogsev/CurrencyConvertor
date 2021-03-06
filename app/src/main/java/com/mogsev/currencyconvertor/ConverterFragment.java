package com.mogsev.currencyconvertor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mogsev.util.Currency;
import com.mogsev.util.CurrencyAdapter;
import com.mogsev.util.CurrencyModel;
import com.mogsev.util.CurrencyURL;

import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhenya on 01.08.2015.
 */
public class ConverterFragment extends Fragment {
    private final static String TAG = "ConverterFragment";
    private final static String CRR = "ConversionRateResult";
    private final static String CRE = "ConversionRateException";
    private final static String GET_URL = "GetURL";
    private final static String URL_WEB_SERVICE = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?";
    private final static String EMPTY = " ";
    private final static String EQUALLY = " = ";

    // Initialize View elements
    private View view;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private TextView textViewFromCurrency;
    private TextView textViewFromCurrencyName;
    private TextView textViewFromRate;
    private TextView textViewToCurrency;
    private TextView textViewToCurrencyName;
    private TextView textViewToRate;
    private Button btnRefreshList;
    private ProgressDialog progressDialog;

    private EditText eValue;
    private TextView tvCalculateTo;
    private TextView tvCalculateFrom;

    private String strFrom;
    private String strTo;
    private BigDecimal numValueFrom;
    private BigDecimal numValueTo;

    private Currency currency;
    private String[] listCode;
    private ArrayList<CurrencyModel> list;
    private CurrencyAdapter currencyAdapter;

    private LinearLayout converterProgressFrom;
    private LinearLayout converterProgressTo;

    ConversionRate conversionRate;
    ConversionRateInverse conversionRateInverse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_converter, container, false);

        // Initialize View elements
        initViewElements();

        // Initialize AsyncTask
        conversionRate = new ConversionRate();
        conversionRateInverse = new ConversionRateInverse();

        // Initialize CurrencyAdapter
        currency = Currency.getInstance();
        listCode = getResources().getStringArray(R.array.currency);
        currency.setListCode(listCode);
        list = currency.getListCurrency();

        currencyAdapter = new CurrencyAdapter(getActivity().getBaseContext(), list);
        spinnerFromCurrency.setAdapter(currencyAdapter);
        spinnerToCurrency.setAdapter(currencyAdapter);

        //***********************************************************
        spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyModel currencyModel = (CurrencyModel) parent.getSelectedItem();
                textViewFromCurrency.setText(currencyModel.getCode());
                textViewFromCurrencyName.setText(currencyModel.getName());
                getRate();
                eValue.setText("");
                tvCalculateFrom.setText("");
                tvCalculateTo.setText("");
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
                getRate();
                eValue.setText("");
                tvCalculateFrom.setText("");
                tvCalculateTo.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerFromCurrency.setSelection(59);
        spinnerToCurrency.setSelection(57);

        refreshListOfCurrency();
        //***********************************************************

        return view;
    }

    /**
     * Initialize View elements
     */
    private void initViewElements() {
        //value = (TextView) view.findViewById(R.id.value);
        spinnerFromCurrency = (Spinner) view.findViewById(R.id.from_currency);
        spinnerToCurrency = (Spinner) view.findViewById(R.id.to_currency);

        textViewFromCurrency = (TextView) view.findViewById(R.id.textViewFromCurrency);
        textViewFromCurrencyName = (TextView) view.findViewById(R.id.textViewFromCurrencyName);
        textViewFromRate = (TextView) view.findViewById(R.id.textViewFromRate);

        textViewToCurrency = (TextView) view.findViewById(R.id.textViewToCurrency);
        textViewToCurrencyName = (TextView) view.findViewById(R.id.textViewToCurrencyName);
        textViewToRate = (TextView) view.findViewById(R.id.textViewToRate);

        btnRefreshList = (Button) view.findViewById(R.id.btn_refresh_bottom);
        btnRefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRate(view);
            }
        });
        btnRefreshList.setFocusableInTouchMode(true);
        btnRefreshList.requestFocus();

        tvCalculateTo = (TextView) view.findViewById(R.id.tvCalculateTo);
        tvCalculateFrom = (TextView) view.findViewById(R.id.tvCalculateFrom);

        eValue = (EditText) view.findViewById(R.id.eValue);
        eValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, String.valueOf(keyCode) + " " + event.getAction());
                if (event.getAction() == 1 || keyCode == KeyEvent.KEYCODE_ENTER) {
                    showCalc();
                    return true;
                } else {
                    return false;
                }
            }
        });

        converterProgressFrom = (LinearLayout) view.findViewById(R.id.converterProgress);
        converterProgressTo = (LinearLayout) view.findViewById(R.id.converterProgress);
    }

    /**
     * Show result
     */
    private void showCalc() {
        if (!eValue.getText().toString().equals("") && eValue.getText() != null) {
            String str = eValue.getText().toString();
            BigDecimal calc = BigDecimal.valueOf(Double.parseDouble(str));
            Log.d("showCalc", str + " " + String.valueOf(calc));
            try {
                calculateValue(calc);
            } catch (Exception ex) {
                Toast.makeText(getActivity().getBaseContext(), getString(R.string.toast_update), Toast.LENGTH_SHORT).show();
            }
        }
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
        strFrom = currencyAdapter.getCode(numFromCurrency);
        strTo = currencyAdapter.getCode(numToCurrency);

        textViewFromCurrency.setText(strFrom);
        textViewToCurrency.setText(strTo);

        textViewFromCurrencyName.setText(currency.getName(strFrom));
        textViewToCurrencyName.setText(currency.getName(strTo));

        switch (i) {
            case CurrencyURL.URL_NORMAL:
                url.append(CurrencyURL.FROM).append(strFrom);
                url.append(CurrencyURL.TO).append(strTo);
                break;
            case CurrencyURL.URL_INVERSE:
                url.append(CurrencyURL.FROM).append(strTo);
                url.append(CurrencyURL.TO).append(strFrom);
                break;
        }
        Log.d(GET_URL, url.toString());
        return url.toString();
    }

    /**
     * Update information
     *
     * @param view
     */
    public void getRate(View view) {
        getRate();
    }

    /**
     * Run AsyncTask
     */
    private void getRate() {
        conversionRate = new ConversionRate();
        conversionRate.execute(getUrl(CurrencyURL.URL_NORMAL)); // Start
        conversionRateInverse = new ConversionRateInverse();
        conversionRateInverse.execute(getUrl(CurrencyURL.URL_INVERSE)); // Start
    }

    /**
     * Return result
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
        return (str.equals("-1") || str.equals("") || str == null) ? getString(R.string.no_data) : str;
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
            try {
                numValueFrom = BigDecimal.valueOf(Double.parseDouble(checkConversionResult(str)));
                textViewFromRate.setText(String.valueOf(checkConversionResult(str)));
                converterProgressFrom.setVisibility(View.GONE);
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            converterProgressFrom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Conversion rate inverse
     */
    private class ConversionRateInverse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return connection(params[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                numValueTo = BigDecimal.valueOf(Double.parseDouble(checkConversionResult(str)));
                textViewToRate.setText(String.valueOf(checkConversionResult(str)));
                calculateValue(new BigDecimal(1));
                converterProgressTo.setVisibility(View.GONE);
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            converterProgressTo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Refresh list of currency
     */
    private void refreshListOfCurrency() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_update));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currencyAdapter.refreshCurrencyAdapter(currency);
                progressDialog.cancel();
            }
        }).start();
    }

    private void calculateValue(BigDecimal calc) {
        Log.d(TAG, "Start calculate");
        BigDecimal resultFrom = calc.multiply(numValueFrom);
        BigDecimal resultTo = calc.multiply(numValueTo);

        // Example 1 USD = 10 UAH
        tvCalculateFrom.setText(String.valueOf(calc));
        tvCalculateFrom.append(EMPTY);
        tvCalculateFrom.append(strFrom);
        tvCalculateFrom.append(EQUALLY);
        tvCalculateFrom.append(String.valueOf(resultFrom));
        tvCalculateFrom.append(EMPTY);
        tvCalculateFrom.append(strTo);

        tvCalculateTo.setText(calc + " " + strTo + " = ");
        tvCalculateTo.append(resultTo + " " + strFrom);
        Log.d(TAG, "calculate end");
    }
}
