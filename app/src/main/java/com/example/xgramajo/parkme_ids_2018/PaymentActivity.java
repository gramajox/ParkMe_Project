package com.example.xgramajo.parkme_ids_2018;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xgramajo.parkme_ids_2018.home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.parking.SetupFragment;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    Button cancelBtn, sumitBtn;

    final int PAYMENT_REQUEST_CODE = 1;

    final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder("4183467068208481", "VGPmDSBjKK2XKwgOpjl1W0ggTRVORn9c")
            .build();

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
            public void onClick(View v) {
                checkout.startPayment(v.getContext(), PAYMENT_REQUEST_CODE);
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