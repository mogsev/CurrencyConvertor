package com.mogsev.load;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.mogsev.currencyconvertor.R;
import com.mogsev.util.CashAdapter;
import com.mogsev.util.CurrencyInformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenya on 05.08.2015.
 */
public class CashFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<CurrencyInformer>> {
    private static final String TAG = "CashFragment";
    private static final int LOADER_CASH = 1;
    private LoaderManager.LoaderCallbacks mCallbacks;
    private SimpleCursorAdapter mAdapter;
    CashAdapter cashAdapter;
    ArrayList<CurrencyInformer> listCash = new ArrayList<>();

    private View view;
    private String[] cityData;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize spinner start
        cityData = getResources().getStringArray(R.array.city);
        spinner = (Spinner) view.findViewById(R.id.spinnerCityInformer);
        spinnerAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, cityData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Initialize spinner end

        //Initialize CashAdapter
        String[] cash = getResources().getStringArray(R.array.cash);
        for (int i = 0; i < cash.length - 1; i++) {
            listCash.add(new CurrencyInformer(cash[i]));
        }
        cashAdapter = new CashAdapter(getActivity(), listCash);
        //cashAdapter = new CashAdapter(getActivity());
        setListAdapter(cashAdapter);

        // Initialize LoadManager
        getLoaderManager().initLoader(LOADER_CASH, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_informer, container, false);
        return view;
    }

    @Override
    public Loader<List<CurrencyInformer>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader start");
        Loader loader = null;
        switch (id) {
            case LOADER_CASH:
                Log.d(TAG, "Create LOADER_CASH");
                loader = new CashLoader(getActivity());
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<CurrencyInformer>> loader, List<CurrencyInformer> data) {
        Log.d(TAG, "onLoadFinished start");
        cashAdapter.setData(data);
    }


    @Override
    public void onLoaderReset(Loader<List<CurrencyInformer>> loader) {
        Log.d(TAG, "onLoaderReset start");
        cashAdapter.setData(null);
    }
}
