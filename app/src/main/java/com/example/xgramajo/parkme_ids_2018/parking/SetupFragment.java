package com.example.xgramajo.parkme_ids_2018.parking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Objects;

public class SetupFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static Spinner spinnerDur, spinnerPatente;
    @SuppressLint("StaticFieldLeak")
    private static TextView montoCalculado;
    String[] arrayMontos;

    String tiempoSeleccionado;
    String numeroPatente;
    String montoAsociado;
    Button contBtn, backBtn;
    ViewPager viewPager;
    LinearLayout setupLayout;
    TextView priceList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_setup, container, false);

        contBtn = (Button) view.findViewById(R.id.btn_continue);
        viewPager = (ViewPager) Objects.requireNonNull(getActivity()).findViewById(R.id.container);
        spinnerPatente = (Spinner) view.findViewById(R.id.spinner_patente);
        spinnerDur = (Spinner) view.findViewById(R.id.spinner_duracion);
        montoCalculado = (TextView) view.findViewById(R.id.txt_monto);

        setupLayout = (LinearLayout) view.findViewById(R.id.layout_setup);
        priceList = (TextView) view.findViewById(R.id.prices_list);

        setInfo(ParkingActivity.prePayment);

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

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
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

    public static String getTime (){
        String time = spinnerDur.getSelectedItem().toString();
        switch (time) {
            case "30 minutos":
                return "30";
            case "1 hora":
                return "60";
            case "1 hora 30 minutos":
                return "90";
            case "2 horas":
                return "120";
            case "2 horas 30 minutos":
                return "150";
            case "3 horas":
                return "180";
        }
        return "0";
    }

    private void setInfo(boolean prePayment) {
        if (prePayment) {

            priceList.setVisibility(View.GONE);

        } else {

            setupLayout.setVisibility(View.GONE);

        }
    }

}
