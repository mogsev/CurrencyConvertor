package com.mogsev.currencyconvertor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogsev.util.CurrencyInformer;
import com.mogsev.util.InformerAdapter;

import java.util.ArrayList;

/**
 * Test fragment
 */
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
        // Initialize ListView
        String[] cash = getResources().getStringArray(R.array.cash);
        for (int i = 0; i < cash.length - 1; i++) {
            listCash.add(new CurrencyInformer(cash[i]));
        }
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
        return view;
    }
}
