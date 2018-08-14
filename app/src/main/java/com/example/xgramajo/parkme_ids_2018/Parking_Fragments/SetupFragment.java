package com.example.xgramajo.parkme_ids_2018.Parking_Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.R;

import java.util.ArrayList;
import java.util.List;

public class SetupFragment extends Fragment {

    private static Spinner spinnerDur, spinnerPatente;
    private static TextView montoCalculado;
    String[] arrayMontos;

    String tiempoSeleccionado;
    String numeroPatente;
    String montoAsociado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_setup, container, false);

        spinnerPatente = (Spinner) view.findViewById(R.id.spinner_patente);
        spinnerDur = (Spinner) view.findViewById(R.id.spinner_duracion);
        montoCalculado = (TextView) view.findViewById(R.id.txt_monto);

       //Fuente: https://es.stackoverflow.com/questions/69656/evento-onclick-en-un-spinner
        spinnerDur.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {
                        tiempoSeleccionado = spn.getSelectedItem().toString();
                        posicion = spn.getSelectedItemPosition();
                        arrayMontos = getResources().getStringArray(R.array.array_montos);
                        montoAsociado = arrayMontos[posicion];
                        montoCalculado.setText(montoAsociado);
                        }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        spinnerPatente.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn2,
                                               android.view.View v2,
                                               int position,
                                               long id2) {
                     numeroPatente = spn2.getSelectedItem().toString();


                    }
                    public void onNothingSelected(AdapterView<?> spn2) {
                    }
                });

        return view;
    }

    public static String getMount(){
        return montoCalculado.getText().toString();
    }

    public static String getSelectedTime (){

        return spinnerDur.getSelectedItem().toString();
    }


    public static String getMatricula (){
        return spinnerPatente.getSelectedItem().toString();

    }














}
