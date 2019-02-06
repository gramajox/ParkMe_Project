package com.example.xgramajo.parkme_ids_2018;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.mercadopago.utils.OneTapSamples;
import com.mercadopago.android.px.core.MercadoPagoCheckout;

import java.util.Objects;

import static com.example.xgramajo.parkme_ids_2018.mercadopago.utils.ExamplesUtils.resolveCheckoutResult;

public class PaymentAdvancedActivity extends AppCompatActivity {

    TextView txtMonto, txtPatente, txtTiempo, txtPasos, txtDir;
    Button cancelBtn, sumitBtn;

    private static final int REQ_CODE_CHECKOUT = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_advanced);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtMonto = findViewById(R.id.txt_monto);
        txtTiempo = findViewById(R.id.txt_tiempo);
        txtPatente = findViewById(R.id.txt_patente);
        txtDir = findViewById(R.id.txt_direction);

        txtPasos = findViewById(R.id.id_pasos);

        cancelBtn = findViewById(R.id.cancel_btn);
        sumitBtn = findViewById(R.id.sumit_btn);

        txtMonto.setText("$ " + (Integer.toString(ParkingClass.getPrice()))); // CHANGED 20190201
        txtPatente.setText(ParkingClass.getPatent());
        txtTiempo.setText(ParkingClass.getTime());
        txtDir.setText(ParkingClass.getDireccion());

        sumitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Le paso el precio a Mercado Pago*/
                OneTapSamples.setAMOUNT(ParkingClass.getPrice());
                MercadoPagoCheckout.Builder mercadoCheckout = OneTapSamples.getMercadoPagoCheckoutBuilder();
                mercadoCheckout.build().startPayment(PaymentAdvancedActivity.this, REQ_CODE_CHECKOUT);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        ParkingClass.setAlreadyPaid(true);

        if (ParkingClass.isPrepayment()) {

            FirebaseController.writeAvaliablePatent(ParkingClass.getPatent(), ParkingClass.getDireccion(), ParkingClass.getEndTime());

            HomeActivity.homeAct.finish();
            HomeActivity.setTimeLeftFragment();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        } else {
            HomeActivity.homeAct.finish();
            HomeActivity.setHomeFragment();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            ParkingChronometerActivity.parkAct.finish();
            finish();
        }

        resolveCheckoutResult(this, requestCode, resultCode, data, REQ_CODE_CHECKOUT);
        super.onActivityResult(requestCode, resultCode, data);

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