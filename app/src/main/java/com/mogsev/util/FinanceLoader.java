package com.mogsev.util;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhenya on 06.08.2015.
 */
public class FinanceLoader extends AsyncTaskLoader<List<Finance>> {
    private static final String URL_CASH = "http://resources.finance.ua/ru/public/currency-cash.xml";
    private static final String TAG = "FinanceLoader";
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
        listFinance.add(new Finance.Builder().title("AVAL").city("Zhytomyr").build());
        listFinance.add(new Finance.Builder().title("PRIVAT").city("Kyiv").build());
        Log.d(TAG, "initData stop" + listFinance.toString());
    }
}
