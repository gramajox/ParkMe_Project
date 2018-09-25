package com.example.xgramajo.parkme_ids_2018.mercadopago.features.plugins;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.xgramajo.parkme_ids_2018.R;
import com.mercadopago.android.px.core.PaymentMethodPlugin;
import com.mercadopago.android.px.model.PaymentMethodInfo;

public class SamplePaymentMethodPlugin implements PaymentMethodPlugin {

    public SamplePaymentMethodPlugin() {
        super();
    }

    @Override
    @NonNull
    public PaymentMethodInfo getPaymentMethodInfo(@NonNull final Context context) {
        return new PaymentMethodInfo(
            getId(),
            "Dinero en cuenta",
            R.drawable.px_sample,
            "Custom payment method"
        );
    }

    @Nullable
    @Override
    public Bundle getFragmentBundle(@NonNull final CheckoutData data,
        @NonNull final Context context) {
        return new Bundle();
    }

    @Override
    public boolean shouldShowFragmentOnSelection() {
        return true;
    }

    @Nullable
    @Override
    public Fragment getFragment(@NonNull final CheckoutData data,
                                @NonNull final Context context) {
        return new SamplePaymentMethodPluginFragment();
    }

    @Override
    public void init(@NonNull final CheckoutData checkoutData) {

    }

    @Override
    public PluginPosition getPluginPosition() {
        return PluginPosition.BOTTOM;
    }

    @NonNull
    @Override
    public String getId() {
        return "account_money";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
