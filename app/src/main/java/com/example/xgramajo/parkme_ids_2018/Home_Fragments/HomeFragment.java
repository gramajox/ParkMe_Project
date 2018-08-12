package com.example.xgramajo.parkme_ids_2018.Home_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xgramajo.parkme_ids_2018.InitCounterActivity;
import com.example.xgramajo.parkme_ids_2018.ParkingActivity;
import com.example.xgramajo.parkme_ids_2018.R;

public class HomeFragment extends Fragment {

    Button btnAdPay;
    Button btnParkCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnAdPay = (Button) view.findViewById(R.id.advanced_payment);
        btnParkCount = (Button) view.findViewById(R.id.park_counter);

        btnAdPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ParkingActivity.class);
                startActivity(myIntent);
            }
        });

        btnParkCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), InitCounterActivity.class);
                startActivity(myIntent);
            }
        });

        return view;
    }

}
