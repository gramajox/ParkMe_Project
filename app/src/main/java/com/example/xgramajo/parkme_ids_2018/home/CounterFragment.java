package com.example.xgramajo.parkme_ids_2018.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PaymentActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.example.xgramajo.parkme_ids_2018.parking.SetupFragment;

import java.util.Locale;

public class CounterFragment extends Fragment {

    CountDownTimer chronometerCDT;
    TextView chronometerCounter, priceCounter, patentCounter;
    long baseTime, activityTime, elapsedTime, freeTime;
    String elapsedTimeFormatted, chargedPriceFormatted, mountToPayStr, mountToShowStr;
    long basePrice, chargedPrice, priceHour, mountToPay;
    int toPay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        // Inicialización de la vista para el cronometro
        chronometerCounter =   (TextView) view.findViewById(R.id.chronometer_counter);
        priceCounter =  (TextView) view.findViewById(R.id.price_counter);
        patentCounter = (TextView) view.findViewById(R.id.patent_counter);

        patentCounter.setText(ParkingClass.getPatent());
        priceHour = 50;
        chargedPrice= 0; // inicializacion de la variable
        basePrice = 2; // precio por segundo
        freeTime = 30000; // tiempo libre en milisegundos que no se cobra

        baseTime = System.currentTimeMillis();
        activityTime = System.currentTimeMillis();



            final CountDownTimer myCountDownTimer = new CountDownTimer(activityTime + 1000, 1000) {
            @Override
            public void onTick(long l) {

                elapsedTime = System.currentTimeMillis() - baseTime;
                // actualiza el texto de la vista del cronometro
                updateChronometerCounter();
                // actualiza el texto de la vista del monto a abonar
                updatePriceCounter();

            }

            @Override
            public void onFinish() {
            // nothing to do
            }
        }.start();



        view.findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCountDownTimer.cancel();
                getElapsedTime();
                if (elapsedTime > freeTime){
                    mountToPay = chargedPrice;
                    mountToPayStr = String.valueOf(mountToPay);
                    mountToShowStr = "Debe pagar: " + mountToPayStr;

                    Toast.makeText(getContext(), mountToShowStr, Toast.LENGTH_LONG).show();
                }
                else {
                    mountToPay = 0;
                    mountToPayStr = String.valueOf(mountToPay);
                    mountToShowStr = "No debe pagar nada: " + mountToPayStr;
                    Toast.makeText(getContext(), mountToShowStr, Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getElapsedTime();
                if (elapsedTime > freeTime){
                    mountToPay = chargedPrice;
                    mountToPayStr = String.valueOf(mountToPay);
                    mountToShowStr = "Debe pagar: " + mountToPayStr;

                    Toast.makeText(getContext(), mountToShowStr, Toast.LENGTH_LONG).show();
                }
                else {
                    mountToPay = 0;
                    mountToPayStr = String.valueOf(mountToPay);
                    mountToShowStr = "No debe pagar nada: " + mountToPayStr;
                    Toast.makeText(getContext(), mountToShowStr, Toast.LENGTH_LONG).show();
                }
                /*seteo el precio del estacionamiento*/
                toPay = (int) mountToPay;
                ParkingClass.setPrice(toPay);
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

    private void updateChronometerCounter(){

        // actualiza el texto de la vista del contador
        int hours = (int) (elapsedTime / 1000) / 3600;
        int minutes = (int) ((elapsedTime / 1000) % 3600) / 60;
        int seconds = (int) (elapsedTime / 1000) % 60;

        // Pasa a un string el tiempo transcurrido formateado en horas minutos y segundos en la forma 00:00:00 o 00:00 si corresponde.

        if (hours > 0) {
            elapsedTimeFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            elapsedTimeFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        chronometerCounter.setText(elapsedTimeFormatted);
        //Pasa a la vista del contador el tiempo restante
    }

    private void updatePriceCounter(){
        chargedPrice = chargedPrice + basePrice;
        // calcula el precio del monto a Abonar
        //chargedPrice = chargedPrice + basePrice;

        // Pasa a un string el precio calculado.
        chargedPriceFormatted = String.valueOf(chargedPrice);

        //Pasa a la vista del monto a Abonar el precio cargado
        priceCounter.setText(chargedPriceFormatted);

    }

    public void getElapsedTime(){
        elapsedTime = System.currentTimeMillis() - baseTime;
    }


}