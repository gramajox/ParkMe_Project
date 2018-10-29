package com.example.xgramajo.parkme_ids_2018.parking;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Objects;

public class SetupFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static TextView montoCalculado;
    String[] arrayMontos;

    String tiempoSeleccionado, numeroPatente;
    int montoAsociado;
    ViewPager viewPager;
    LinearLayout setupLayout;
    TextView priceList;
    Boolean timeIsSelected;
    Button contBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_setup, container, false);

        viewPager = (ViewPager) Objects.requireNonNull(getActivity()).findViewById(R.id.container);

        Spinner spinnerPatente = (Spinner) view.findViewById(R.id.spinner_patente);
        Spinner spinnerDur = (Spinner) view.findViewById(R.id.spinner_duracion);
        montoCalculado = (TextView) view.findViewById(R.id.txt_monto);

        setupLayout = (LinearLayout) view.findViewById(R.id.layout_setup);
        //priceList = (TextView) view.findViewById(R.id.prices_list);

        contBtn = (Button) view.findViewById(R.id.btn_continue);

        setInfo(ParkingClass.isPrepayment());

        //Fuente: https://es.stackoverflow.com/questions/69656/evento-onclick-en-un-spinner
        spinnerDur.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {

                        ((TextView) v).setTextSize(20);

                        tiempoSeleccionado = spn.getSelectedItem().toString();
                        posicion = spn.getSelectedItemPosition();

                            if (posicion == 0){

                                ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));

                                contBtn.setBackgroundColor(Color.GRAY);

                                timeIsSelected = false;
                            }
                            else {
                                ((TextView) v).setTextColor(Color.BLACK);
                                contBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                                timeIsSelected = true;
                            }

                        arrayMontos = getResources().getStringArray(R.array.array_montos);
                        montoCalculado.setText("$ " + arrayMontos[posicion]);

                        /*Porque el primer elemento es un string $$$ y no puedo pasarlo a INT*/
                        if (posicion != 0) {
                            montoAsociado = Integer.parseInt(arrayMontos[posicion]);
                        }
                        }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });

        spinnerPatente.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn2,
                                               android.view.View v2,
                                               int posicion2,
                                               long id2) {

                        ((TextView) v2).setTextSize(20);

                        numeroPatente = spn2.getSelectedItem().toString();

                    }
                    public void onNothingSelected(AdapterView<?> spn2) {
                    }
                });

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeIsSelected == null) {

                    ParkingClass.setPatent(numeroPatente);

                    viewPager.setCurrentItem(1);
                } else {
                    if (!timeIsSelected){
                        Toast.makeText(getActivity(), "Seleccione su tiempo", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ParkingClass.setPatent(numeroPatente);
                        ParkingClass.setPrice(montoAsociado);
                        ParkingClass.setTime(tiempoSeleccionado);

                        viewPager.setCurrentItem(1);
                    }
                }
            }


        });



        return view;
    }

    private void setInfo(boolean prePayment) {
        if (prePayment) {

            priceList.setVisibility(View.GONE);

        } else {

            setupLayout.setVisibility(View.GONE);

        }
    }

}
