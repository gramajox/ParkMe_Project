package com.example.xgramajo.parkme_ids_2018.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.FirebaseController;
import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.PaymentAdvancedActivity;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Locale;
import java.util.Objects;

import static java.lang.String.valueOf;

public class ChronometerFragment extends Fragment {

    TextView chronometerCounter, priceCounter, patentCounter, txtDir;
    long baseTime, activityTime, elapsedTime, freeTime;
    String price, elapsedTimeFormatted;
    long   chargedPrice, priceHour, mountToPay;
    int mount, toPay;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        // Inicialización de la vista para el cronometro
        chronometerCounter = view.findViewById(R.id.chronometer_counter);
        priceCounter =  view.findViewById(R.id.price_counter);
        patentCounter = view.findViewById(R.id.patent_counter);
        txtDir = view.findViewById(R.id.txt_direction);

        txtDir.setText(ParkingClass.getDireccion());

        patentCounter.setText(ParkingClass.getPatent());
        chargedPrice= 0; // inicializacion de la variable
        mount = 0; // inicializacion de la variable
        freeTime = 3000; // 120000 segundos es el tiempo libre en milisegundos que no se cobra
        // para actualizar el freeTime hay que actualizar esta variable, el if (secs < 3) del getPrice() y case 3: del addMount()


        baseTime = System.currentTimeMillis();
        activityTime = System.currentTimeMillis();



            final CountDownTimer myCountDownTimer = new CountDownTimer(activityTime + 1000, 1000) {
            @Override
            public void onTick(long l) {

                elapsedTime = System.currentTimeMillis() - baseTime;
                // actualiza el texto de la vista del cronometro
                updateChronometerCounter();
                // actualiza el texto de la vista del monto a abonar
                if (elapsedTime > freeTime) {
                    updatePriceCounter();
                }
            }

            @Override
            public void onFinish() {
            // nothing to do
            }
        }.start();

        view.findViewById(R.id.pay_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseController.removeAvaliablePatent(ParkingClass.getPatent());

                getElapsedTime();
                if (elapsedTime > freeTime){
                    mountToPay = chargedPrice;

                    toPay = (int) mountToPay;
                    ParkingClass.setPrice(toPay);
                    ParkingClass.setTime(elapsedTimeFormatted);

                    startActivity(new Intent(getContext(), PaymentAdvancedActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }
                else {
                    //mountToPay = 0;
                    HomeActivity.setHomeFragment();
                    startActivity(new Intent(getContext(), HomeActivity.class));
                    Objects.requireNonNull(getActivity()).finish();
                }

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

    public void getElapsedTime(){
        elapsedTime = System.currentTimeMillis() - baseTime;
    }


    private void updatePriceCounter(){
       //Pasa a la vista del monto a Abonar el precio cargado

        price = getPrice();
        priceCounter.setText("$ " + price);
        chargedPrice = Long.valueOf(price);

    }

    public String getPrice(){

        elapsedTime = System.currentTimeMillis() - baseTime;
        int secs = (int) (elapsedTime / 1000);

        if (secs < 3){
            // es el tiempo libre que el contador no actualiza el monto
            mount =0;
        }else
        {
            if (addMount(secs) == 0) {
                // actualiza el monto con valor cero
                mount = mount + addMount(secs);
            }
            else {
                // actualiza el monto con el valor del caso
                mount = addMount(secs);
            }
        }


        return valueOf(mount);
    }

    public int addMount(int secs){
        switch (secs) {
            case 3: //tiempo libre 2 minutos x 60 segundos = 120 segundos
                return 5; // como utilizamos un case, pra que no muestre 0 despues del tiempo libre y antes del primer caso que se cumple
            case 10: //30 minutos x 60 segundos = 1800 segundos
                return 5;
            case 15: //60 minutos x 60 segundos = 3600 segundos
                return 10;
            case 20: //90 minutos x 60 segundos = 5400 segundos
                return 15;
            case 25: //120 minutos x 60 segundos = 7200 segundos
                return 20;
            case 30: //150 minutos x 60 segundos = 9000 segundos
                return 25;
            case 35: // 180 minutos x 60 segundos = 10800 segundos
                return 30;
        }
    return 0;
    }

}

