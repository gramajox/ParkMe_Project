package com.example.xgramajo.parkme_ids_2018.mercadopago.features.plugins;

import android.content.Context;

import com.example.xgramajo.parkme_ids_2018.R;


public class SampleResourcesProvider implements SampleResources {

    private final Context context;

    SampleResourcesProvider(final Context context) {
        this.context = context;
    }

    @Override
    public String getPasswordErrorMessage() {
        return context.getString(R.string.auth_error_password);
    }

    @Override
    public String getPasswordRequiredMessage() {
        return context.getString(R.string.auth_error_password_required);
    }
}
