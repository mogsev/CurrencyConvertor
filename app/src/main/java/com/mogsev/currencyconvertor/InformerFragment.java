package com.mogsev.currencyconvertor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogsev.util.CashAdapter;
import com.mogsev.util.Currency;
import com.mogsev.util.CurrencyInformer;
import com.mogsev.util.InformerAdapter;

import java.util.ArrayList;

public class InformerFragment extends Fragment {
    private static final String TAG = "InformerFragment";

    private View view;
    private Spinner spinner;
    private ScrollView scrollView;
    private TextView tvName;
    private TextView tvCode;
    private TextView tvBuy;
    private TextView tvSale;
    private TextView tvBuyDelta;
    private TextView tvSaleDelta;
    private ProgressDialog progressDialog;

    private String[] cityData;
    private ArrayAdapter<String> adapter;
    InformerAdapter informerAdapter = new InformerAdapter();
    ArrayList<CurrencyInformer> listCash = new ArrayList<>();

    public static InformerFragment newInstance() {
        InformerFragment fragment = new InformerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public InformerFragment() {
        // Required empty public constructor
    }

    /**
     * Initialize view elements
     */
    private void initView() {
        spinner = (Spinner) view.findViewById(R.id.spinnerCityInformer);
        //scrollView = (ScrollView) view.findViewById(R.id.scrollViewInformer);
        tvName = (TextView) view.findViewById(R.id.tvInformerName);
        tvCode = (TextView) view.findViewById(R.id.tvInformerCode);
        tvBuy = (TextView) view.findViewById(R.id.tvInformerBuy);
        tvBuyDelta = (TextView) view.findViewById(R.id.tvInformerBuyDelta);
        tvSale = (TextView) view.findViewById(R.id.tvInformerSale);
        tvSaleDelta = (TextView) view.findViewById(R.id.tvInformerSaleDelta);
    }

    /**
     * Initialize data
     */
    private void initData() {
        // Initialize spinner
        cityData = getResources().getStringArray(R.array.city);
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, cityData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //CurrencyInformer cur = new CurrencyInformer.Builder("UAH").buy("24.00").sale("23.00").buyDelta("-0.235").saleDelta("-0568").name("Grivna").build();
        //Log.d(TAG, cur.toString());

        // Initialize ListView
        String[] cash = getResources().getStringArray(R.array.cash);
        for (int i = 0; i < cash.length - 1; i++) {
            listCash.add(new CurrencyInformer(cash[i]));
        }
        CashAdapter cashAdapter = new CashAdapter(getActivity().getBaseContext(), listCash);
        ListView listView = (ListView) view.findViewById(R.id.listViewInformer);
        listView.setAdapter(cashAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_informer, container, false);
        initView();
        initData();

        //******************************************************************************





        //*******************************************************************************
        return view;
    }

    private View getViewInformer() {
        return null;
    }


}
