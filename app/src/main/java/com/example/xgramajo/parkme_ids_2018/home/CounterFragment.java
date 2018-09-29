package com.example.xgramajo.parkme_ids_2018.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PaymentActivity;
import com.example.xgramajo.parkme_ids_2018.R;

public class CounterFragment extends Fragment {

    Chronometer chronometer;
    TextView priceCounter, patentCounter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        chronometer =   (Chronometer) view.findViewById(R.id.counter_id);
        priceCounter =  (TextView) view.findViewById(R.id.price_counter);
        patentCounter = (TextView) view.findViewById(R.id.patent_counter);

        chronometer.start();

        view.findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
            }
        });

        view.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*seteo el precio del estacionamiento*/
                ParkingClass.setPrice(999);
                /*Seteo PRE_PAYMENT false para que al terminar mercadopago vaya al home y no al timeLeft*/
                PaymentActivity.setPRE_PAYMENT(false);
                startActivity(new Intent(getContext(), PaymentActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}