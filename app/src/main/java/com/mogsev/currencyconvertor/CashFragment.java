package com.mogsev.currencyconvertor;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mogsev.util.CashLoader;
import com.mogsev.util.CashAdapter;
import com.mogsev.util.CurrencyInformer;

import java.util.List;

/**
 * Created by zhenya on 05.08.2015.
 */
public class CashFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<CurrencyInformer>> {
    private static final String TAG = "CashFragment";
    private static final int LOADER_CASH = 1;
    CashAdapter cashAdapter;
    private LinearLayout linearProgress;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Initialize progress bar loader
        linearProgress = (LinearLayout) view.findViewById(R.id.loaderProgress);

        // Initialize CashAdapter
        cashAdapter = new CashAdapter(getActivity());
        setListAdapter(cashAdapter);

        // Initialize LoadManager
        getLoaderManager().initLoader(LOADER_CASH, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cash, container, false);
        return view;
    }

    @Override
    public Loader<List<CurrencyInformer>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader start");
        // Show progress bar
        linearProgress.setVisibility(View.VISIBLE);
        return new CashLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<CurrencyInformer>> loader, List<CurrencyInformer> data) {
        Log.d(TAG, "onLoadFinished start");
        // Set the new data in the adapter.
        cashAdapter.setData(data);
        // Hide progress bar
        linearProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<CurrencyInformer>> loader) {
        Log.d(TAG, "onLoaderReset start");
        cashAdapter.setData(null);
    }
}
