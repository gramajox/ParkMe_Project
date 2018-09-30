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

import static android.view.View.GONE;
import static com.example.xgramajo.parkme_ids_2018.mercadopago.utils.ExamplesUtils.resolveCheckoutResult;

import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    Boolean backPressed;

    Button cancelBtn, sumitBtn;

    private static final int REQ_CODE_CHECKOUT = 1;

    private static boolean PRE_PAYMENT = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        backPressed = false;

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        sumitBtn = (Button) findViewById(R.id.sumit_btn);

        sumitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Le paso el precio a Mercado Pago*/
                OneTapSamples.setAMOUNT(ParkingClass.getPrice());
                MercadoPagoCheckout.Builder mercadoCheckout = OneTapSamples.getMercadoPagoCheckoutBuilder();
                mercadoCheckout.build().startPayment(PaymentActivity.this, REQ_CODE_CHECKOUT);
            }
        });

        if(!PRE_PAYMENT) {
            cancelBtn.setVisibility(GONE);
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.setHomeFragment();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        ((TextView) findViewById(R.id.mp_results)).setText(
                        "MATRÍCULA\n" + ParkingClass.getPatent()+ "\n\n" +
                        "TIEMPO DE HABILITACIÓN\n" + ParkingClass.getTime() + "\n\n" +
                        "MONTO A ABONAR\n" + "$ " + Integer.toString(ParkingClass.getPrice()));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (!backPressed) {
            ParkingClass.setAlreadyPaid(true);

            if (PRE_PAYMENT) {
                HomeActivity.setTimeLeftFragment();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            } else {
                HomeActivity.setHomeFragment();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
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
        backPressed = true;
        super.onBackPressed();
    }

    public static void setPRE_PAYMENT(Boolean b) {
        PRE_PAYMENT = b;
    }
}