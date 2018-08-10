package com.example.xgramajo.parkme_ids_2018.Parking_Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.R;

public class SummaryFragment extends Fragment {


    private static TextView matricula;
    private static TextView time;
    private static TextView mount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_summary, container, false);

       matricula = (TextView) view.findViewById(R.id.txt_res_msj02);
       matricula.setText(SetupFragment.getMatricula());

        time = (TextView) view.findViewById(R.id.txt_res_msj03);
        time.setText(SetupFragment.getSelectedTime());

        mount = (TextView) view.findViewById(R.id.textView4);
        mount.setText(SetupFragment.getMount());


        return view;
    }



}
