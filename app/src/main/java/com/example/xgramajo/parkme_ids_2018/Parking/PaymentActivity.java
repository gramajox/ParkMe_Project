package com.example.xgramajo.parkme_ids_2018.Parking;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xgramajo.parkme_ids_2018.Home.HomeActivity;
import com.example.xgramajo.parkme_ids_2018.R;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.core.CustomServer;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Payment;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

    }

    public void submit(View view) {
        // Create a map with payment’s details.
        Map<String, Object> preferenceMap = new HashMap<>();
        preferenceMap.put("item_id", "1");
        preferenceMap.put("amount", new BigDecimal(10));
        preferenceMap.put("currency_id", "ARS");
        preferenceMap.put("payer_email", "procrast.thor@gmail.com");

        LayoutUtil.showProgressLayout(activity);
        CustomServer.createCheckoutPreference(activity, "https://us-central1-ingsoftproject-f89c2.cloudfunctions.net/paymentMercadoPago/", "/paymentMercadoPago", preferenceMap, new Callback<CheckoutPreference>() {
            @Override
            public void success(CheckoutPreference checkoutPreference) {
                startMercadoPagoCheckout(checkoutPreference);
                LayoutUtil.showRegularLayout(activity);
            }

            @Override
            public void failure(ApiException apiException) {
                // Ups, something went wrong

                Log.d("PaymentActivity",apiException.toString());

                Toast.makeText(PaymentActivity.this,"ApiException: " + apiException, Toast.LENGTH_LONG).show();
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
            }
        });
    }

    private void startMercadoPagoCheckout(CheckoutPreference checkoutPreference) {
        new MercadoPagoCheckout.Builder()
                .setActivity(activity)
                .setPublicKey("TEST-e788371b-6434-4035-80f4-358aea2066e8").setCheckoutPreference(checkoutPreference)
                .startForPayment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MercadoPagoCheckout.CHECKOUT_REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                Payment payment = JsonUtil.getInstance().fromJson(data.getStringExtra("payment"), Payment.class);
                ((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getStatus());
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    MercadoPagoError mercadoPagoError = JsonUtil.getInstance().fromJson(data.getStringExtra("mercadoPagoError"), MercadoPagoError.class);
                    ((TextView) findViewById(R.id.mp_results)).setText("Error: " +  mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

}

