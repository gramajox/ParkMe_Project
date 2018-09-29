package com.example.xgramajo.parkme_ids_2018.mercadopago.features.plugins;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mercadopago.android.px.internal.util.TextUtil;

class SamplePaymentMethodPresenter {

    private static final String PASSWORD = "123";
    private static final int DELAY_MILLIS = 2000;
    /* default */ @Nullable
    private
    SamplePaymentMethodPluginFragment samplePaymentMethodPluginFragment;
    /* default */ private final SampleResources resources;
    /* default */ SampleState state;

    SamplePaymentMethodPresenter(
            @NonNull final SampleResources resources) {
        this.resources = resources;
        state = new SamplePaymentMethodPresenter.SampleState(false, "", "");

    }

    void authenticate(final String password) {

        if (TextUtil.isEmpty(password)) {
            state = new SampleState(false,
                resources.getPasswordRequiredMessage(),
                "");
            update();
        } else {

            state = new SampleState(true, null, password);
            update();
            // Simular llamada API....
            // En otro thread
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(DELAY_MILLIS);
                    } catch (final InterruptedException e) {
                        //nada
                    }

                    if (PASSWORD.equals(password)) {
                        if (samplePaymentMethodPluginFragment != null) {
                            samplePaymentMethodPluginFragment.next();
                        }
                    } else {
                        state = new SampleState(false, resources.getPasswordErrorMessage(), password);
                        update();
                    }
                }
            }).start();
        }
    }

    /* default */
    private void update() {
        if (samplePaymentMethodPluginFragment != null) {
            samplePaymentMethodPluginFragment.update(state);
        }
    }

    void init(final SamplePaymentMethodPluginFragment samplePaymentMethodPluginFragment) {
        this.samplePaymentMethodPluginFragment = samplePaymentMethodPluginFragment;
        this.samplePaymentMethodPluginFragment.update(state);
    }

    public static class SampleState {

        final boolean authenticating;
        public final String password;
        final String errorMessage;

        SampleState(final boolean authenticating, final String errorMessage, final String password) {
            this.authenticating = authenticating;
            this.errorMessage = errorMessage;
            this.password = password;
        }
    }
}