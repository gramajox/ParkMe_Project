package com.example.xgramajo.parkme_ids_2018.mercadopago.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.configuration.ReviewAndConfirmConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.core.MercadoPagoCheckout.Builder;
import com.mercadopago.android.px.internal.util.ViewUtils;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.tracking.PXEventListener;
import com.mercadopago.android.px.tracking.internal.MPTracker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

public final class ExamplesUtils {

    private static final String REQUESTED_CODE_MESSAGE = "Requested code: ";
    private static final String PAYMENT_WITH_STATUS_MESSAGE = "Payment with status: ";
    private static final String RESULT_CODE_MESSAGE = " Result code: ";

    public static void resolveCheckoutResult(final Activity context, final int requestCode, final int resultCode,
        final Intent data, final int reqCodeCheckout) {
        ViewUtils.showRegularLayout(context);

        if (requestCode == reqCodeCheckout) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                Toast.makeText(context, new StringBuilder()
                    .append(PAYMENT_WITH_STATUS_MESSAGE)
                    .append(payment), Toast.LENGTH_LONG)
                    .show();
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null
                    && data.getExtras() != null
                    && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                        (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    Toast.makeText(context, "Error: " + mercadoPagoError, Toast.LENGTH_LONG)
                        .show();
                } else {
                    Toast.makeText(context, new StringBuilder()
                        .append("Cancel - ")
                        .append(REQUESTED_CODE_MESSAGE)
                        .append(requestCode)
                        .append(RESULT_CODE_MESSAGE)

                        .append(resultCode), Toast.LENGTH_LONG)
                        .show();
                }
            } else {

                Toast.makeText(context, new StringBuilder()
                    .append(REQUESTED_CODE_MESSAGE)
                    .append(requestCode)
                    .append(RESULT_CODE_MESSAGE)
                    .append(resultCode), Toast.LENGTH_LONG)
                    .show();
            }
        }
    }
}