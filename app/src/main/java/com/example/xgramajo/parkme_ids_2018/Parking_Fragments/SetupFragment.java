package com.example.xgramajo.parkme_ids_2018.Parking_Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.R;

public class SetupFragment extends Fragment {

    private static Spinner spinnerDur, spinnerPatente;
    private static TextView montoCalculado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_setup, container, false);

        spinnerPatente = (Spinner) view.findViewById(R.id.spinner_patente);
        spinnerDur = (Spinner) view.findViewById(R.id.spinner_duracion);
        montoCalculado = (TextView) view.findViewById(R.id.txt_monto);

        String itemSelect = spinnerDur.getSelectedItem().toString();

        switch(itemSelect) {
                case "30 minutos":
                    montoCalculado.setText("$20");
                    break;
                case "1 hora":
                    montoCalculado.setText("$40");
                    break;
                case "1 hora 30 minutos":
                    montoCalculado.setText("$60");
                default:
                    montoCalculado.setText("_______");
        }

        return view;
    }

    public static String getPrice() {
        return montoCalculado.getText().toString();
    }

    public static String getSelectedTime() {
        return spinnerDur.getSelectedItem().toString();
    }

    public static String getPatente() {
        return spinnerPatente.getSelectedItem().toString();
    }

}
