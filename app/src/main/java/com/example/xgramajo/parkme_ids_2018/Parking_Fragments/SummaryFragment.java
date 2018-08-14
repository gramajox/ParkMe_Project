package com.example.xgramajo.parkme_ids_2018.Parking_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.Login.LoginActivity;
import com.example.xgramajo.parkme_ids_2018.R;

public class SummaryFragment extends Fragment {

    private static TextView matricula,time,mount;
    Button payBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_summary, container, false);

        matricula = (TextView) view.findViewById(R.id.txt_res_msj02);
        time = (TextView) view.findViewById(R.id.txt_res_msj03);
        mount = (TextView) view.findViewById(R.id.textView4);
        payBtn = (Button) view.findViewById(R.id.pay_btn);

        matricula.setText(SetupFragment.getMatricula());
        time.setText(SetupFragment.getSelectedTime());
        mount.setText(SetupFragment.getMount());

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mercado Pago -> DB Firebase", Toast.LENGTH_LONG).show();
                HomeActivity.setTimeLeftFragment();
                Intent myIntent = new Intent(getContext(), HomeActivity.class);
                startActivity(myIntent);
            }
        });

        return view;
    }

}
