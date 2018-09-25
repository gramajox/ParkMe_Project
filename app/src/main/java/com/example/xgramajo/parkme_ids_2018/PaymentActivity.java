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
import com.example.xgramajo.parkme_ids_2018.parking.SetupFragment;
import com.mercadopago.android.px.core.MercadoPagoCheckout;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    Button cancelBtn, sumitBtn;

    private static final int REQ_CODE_CHECKOUT = 1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        sumitBtn = (Button) findViewById(R.id.sumit_btn);

        sumitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/**
                //startActivity(new Intent(getApplicationContext(), SelectCheckoutActivity.class));*/
                MercadoPagoCheckout.Builder mercadoCheckout = OneTapSamples.getMercadoPagoCheckoutBuilder();
                mercadoCheckout.build().startPayment(PaymentActivity.this, REQ_CODE_CHECKOUT);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setHomeFragment();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        ((TextView) findViewById(R.id.mp_results)).setText(
                "MATRÍCULA\n" + SetupFragment.getMatricula()+ "\n\n" +
                        "TIEMPO DE HABILITACIÓN\n" + SetupFragment.getSelectedTime() + "\n\n" +
                        "MONTO A ABONAR\n" + SetupFragment.getMount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}