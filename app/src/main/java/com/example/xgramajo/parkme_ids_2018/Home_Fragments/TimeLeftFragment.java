package com.example.xgramajo.parkme_ids_2018.Home_Fragments;

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

import com.example.xgramajo.parkme_ids_2018.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;

public class TimeLeftFragment extends Fragment {

    Chronometer chronometer;
    Button finishBtn;
    TextView patent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_left, container, false);

        chronometer =   (Chronometer) view.findViewById(R.id.counter_id);
        finishBtn =     (Button) view.findViewById(R.id.finish_btn);
        patent =        (TextView) view.findViewById(R.id.patent_counter);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "DB Firebase", Toast.LENGTH_LONG).show();
                HomeActivity.setHomeFragment();
                startActivity(new Intent(getContext(), HomeActivity.class));
            }
        });

        return view;
    }

}
