package com.example.xgramajo.parkme_ids_2018.mercadopago.utils;

import android.support.annotation.NonNull;

import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.PaymentMethodPlugin;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.example.xgramajo.parkme_ids_2018.mercadopago.features.plugins.SamplePaymentMethodPlugin;

final class PaymentConfigurationUtils {

    private static PaymentConfiguration create(
            @NonNull final PaymentProcessor paymentProcessor,
            @NonNull final PaymentMethodPlugin paymentMethodPlugin) {
        return new PaymentConfiguration.Builder(paymentProcessor)
            .addPaymentMethodPlugin(paymentMethodPlugin)
            .build();
    }

    static PaymentConfiguration createWithPlugin(
            @NonNull final PaymentProcessor paymentProcessor) {
        return create(paymentProcessor, new SamplePaymentMethodPlugin());
    }
}
