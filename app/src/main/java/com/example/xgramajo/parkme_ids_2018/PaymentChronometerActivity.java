package com.example.xgramajo.parkme_ids_2018;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;

import java.util.Objects;

public class PaymentChronometerActivity extends AppCompatActivity {

    TextView txtPatente, txtPasos, txtDir;
    Button cancelBtn, chronometerBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_chronometer);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtPatente = findViewById(R.id.txt_patente);
        txtDir = findViewById(R.id.txt_direction);

        txtPasos = findViewById(R.id.id_pasos);

        cancelBtn = findViewById(R.id.cancel_btn);
        chronometerBtn = findViewById(R.id.chronometer_btn);

        txtPatente.setText(ParkingClass.getPatent());
        txtDir.setText(ParkingClass.getDireccion());

        chronometerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseController.writeAvaliablePatent(ParkingClass.getPatent(), ParkingClass.getDireccion(), ParkingClass.getEndTime());

                HomeActivity.setCounterFragment();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingChronometerActivity.parkAct.finish();
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}