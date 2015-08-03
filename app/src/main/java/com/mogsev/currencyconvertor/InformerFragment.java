package com.mogsev.currencyconvertor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mogsev.util.CurrencyInformer;

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

    public static InformerFragment newInstance() {
        InformerFragment fragment = new InformerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public InformerFragment() {
        // Required empty public constructor

    }

    public void initView() {
        spinner = (Spinner) view.findViewById(R.id.spinnerCityInformer);
        scrollView = (ScrollView) view.findViewById(R.id.scrollViewInformer);
        tvName = (TextView) view.findViewById(R.id.tvInformerName);
        tvCode = (TextView) view.findViewById(R.id.tvInformerCode);
        tvBuy = (TextView) view.findViewById(R.id.tvInformerBuy);
        tvBuyDelta = (TextView) view.findViewById(R.id.tvInformerBuyDelta);
        tvSale = (TextView) view.findViewById(R.id.tvInformerSale);
        tvSaleDelta = (TextView) view.findViewById(R.id.tvInformerSaleDelta);
        CurrencyInformer cur = new CurrencyInformer.Builder("UAH")
                .buy("24.00").sale("23.00").buyDelta("-0.235").saleDelta("-0568").name("Grivna").build();
        Log.d(TAG, cur.toString());

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
        return view;
    }

}
