package com.example.xgramajo.parkme_ids_2018.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.R;
import com.example.xgramajo.parkme_ids_2018.parking.SetupFragment;

public class CounterFragment extends Fragment {

    Chronometer chronometer;
    TextView priceCounter, patentCounter;
    Button stopBtn, payBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        chronometer =   (Chronometer) view.findViewById(R.id.counter_id);
        priceCounter =  (TextView) view.findViewById(R.id.price_counter);
        patentCounter = (TextView) view.findViewById(R.id.patent_counter);
        stopBtn =       (Button) view.findViewById(R.id.stop_btn);
        payBtn =        (Button) view.findViewById(R.id.pay_btn);

        patentCounter.setText(TimeLeftFragment.getMatricula());

        chronometer.start();



        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mercado Pago -> DB Firebase", Toast.LENGTH_LONG).show();
                HomeActivity.setHomeFragment();
                startActivity(new Intent(getContext(), HomeActivity.class));
            }
        });



        return view;
    }

}