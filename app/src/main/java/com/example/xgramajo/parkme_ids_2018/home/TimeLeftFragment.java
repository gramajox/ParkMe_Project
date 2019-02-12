package com.example.xgramajo.parkme_ids_2018.home;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.NotificationHelper;
import com.example.xgramajo.parkme_ids_2018.ParkingClass;
import com.example.xgramajo.parkme_ids_2018.FirebaseController;
import com.example.xgramajo.parkme_ids_2018.R;

import java.util.Locale;
import java.util.Objects;

public class TimeLeftFragment extends Fragment {

    private TextView mTextViewCountDown, txtDir;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;

    // 15 minutos en milisegundos
    final long FIFTEEN_MINUTES = 1000*60*15;
    // Bool para saber si ya se envió esta notificación
    private boolean isFirstNotificationSent = false;

    // 5 minutos en milisegundos
    final long FIVE_MINUTES = 1000*60*5;
    // Bool para saber si ya se envió esta notificación
    private boolean isSecondNotificationSent = false;

    // Para enviar notificaciones sobre el tiempo restante
    private NotificationHelper mNotificationHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_left, container, false);

        TextView mPatentCounter = (TextView) view.findViewById(R.id.patent_counter);

        // Inicialización de la vista para el contador
        mTextViewCountDown = (TextView) view.findViewById(R.id.text_view_countdown);

        //mTextViewMontoAbonar = (TextView) view.findViewById(R.id.text_view_montoAbonar);

        mPatentCounter.setText(ParkingClass.getPatent());

        txtDir = view.findViewById(R.id.txt_direction);

        txtDir.setText(ParkingClass.getDireccion());

        // Obtenemos el tiempo y lo almacenamos como entrada
        String input = (ParkingClass.getOnlyNumberTime());

        // Convierte el tiempo en texto a milisegundos
        long millisInput = Long.parseLong(input) * 60000;

        // Pasa la variable en milisegundos al tiempo inicial en milisegundos del contador
        setTime(millisInput);

        // Inicia el temporizador
        startTimer();

        view.findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseController.removeAvaliablePatent(ParkingClass.getPatent());

                HomeActivity.setHomeFragment();
                startActivity(new Intent(getContext(), HomeActivity.class));
                Objects.requireNonNull(getActivity()).finish();

                //Eliminar la notificación del cronómetro
                removeTimeLeftNotification();
            }
        });

        //Inicializa el notification helper
        mNotificationHelper = new NotificationHelper(getActivity());

        return view;
    }

    private void setTime(long milliseconds){
        //  asigna al tiempo inicial los milisegundos
        mStartTimeInMillis = milliseconds;
        // reinicia el temporizador
        resetTimer();
    }

    private void startTimer(){

        // Tiempo final es igual al tiempo actual del sistema mas el tiempo restante
        long mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        // inicia el temporizador
        // metodo disparado a intervalos regulares marcados en el intervalo del contador cada 1000 milisegundos (1 segundo)

        CountDownTimer mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                // actualiza el texto de la vista del contador
                updateCountDownText();

                //enviar notificacion cuando queden 15 minutos
                if (mStartTimeInMillis > FIFTEEN_MINUTES && mTimeLeftInMillis < FIFTEEN_MINUTES && !isFirstNotificationSent) {
                    sendTimeLeftNotification("15");
                    isFirstNotificationSent = true;
                }

                //enviar notificacion cuando queden 5 minutos
                if (mStartTimeInMillis > FIVE_MINUTES && mTimeLeftInMillis < FIVE_MINUTES && !isSecondNotificationSent) {
                    sendTimeLeftNotification("5");
                    isSecondNotificationSent = true;
                }
            }

            @Override
            public void onFinish() {
                // metodo disparado cuando se termina el tiempo

                // Esto lo asignamos manualmente porque la clase CountDownTimer termina en 00:01
                mTimeLeftInMillis = 0;
                // ver mas info en https://www.youtube.com/watch?v=MDuGwI6P-X8&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd
                // Y en https://stackoverflow.com/questions/6810416/android-countdowntimer-shows-1-for-two-seconds

                // actualiza el texto de la vista del contador
                updateCountDownText();
            }
        }.start();
        //mTimerRunning = true; // activa el temporizador corriendo
    }

    //private void pauseTimer(){
        //mCountDownTimer.cancel();
        //cancela el contador
        //mTimerRunning = false;
        // desactiva el temporizador corriendo

    //}

    private void resetTimer(){
        // pone el tiempo restante al tiempo inicial
        mTimeLeftInMillis = mStartTimeInMillis;
        // actualiza el texto de la vista del contador
        updateCountDownText();

   }

    private void updateCountDownText(){

        // actualiza el texto de la vista del contador
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        // Pasa a un string el tiempo restante formateado en horas minutos y segundos en la forma 00:00:00 o 00:00 si corresponde.
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
        //Pasa a la vista del contador el tiempo restante
    }

    public void sendTimeLeftNotification(String minutes) {

        final Intent notificationIntent = new Intent(getActivity(), HomeActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification("Atención", "Te quedan " + minutes + " minutos de estacionamiento.");
        nb.setContentIntent(contentIntent)
            .setAutoCancel(true);

        mNotificationHelper.getManager().notify(1, nb.build());
    }

    public void removeTimeLeftNotification() {

        mNotificationHelper.getManager().cancel(1);
    }

}
