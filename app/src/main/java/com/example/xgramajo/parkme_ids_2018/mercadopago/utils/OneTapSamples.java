package com.example.xgramajo.parkme_ids_2018.mercadopago.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.configuration.DiscountConfiguration;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.core.PaymentProcessor;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.preferences.CheckoutPreference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.xgramajo.parkme_ids_2018.mercadopago.features.plugins.SamplePaymentProcessorNoView;
import static com.example.xgramajo.parkme_ids_2018.mercadopago.utils.PaymentUtils.getBusinessPaymentApproved;
import static com.example.xgramajo.parkme_ids_2018.mercadopago.utils.PaymentUtils.getGenericPaymentApprovedAccountMoney;

public final class OneTapSamples {

    private static final String ONE_TAP_PAYER_2_ACCESS_TOKEN =
        "APP_USR-3456261032857473-073011-c49291f53879f65accfaf28764e55f3e-340764447";
    private static final String ONE_TAP_MERCHANT_PUBLIC_KEY = "APP_USR-648a260d-6fd9-4ad7-9284-90f22262c18d";
    private static final String PAYER_EMAIL_DUMMY = "prueba@gmail.com";

    private OneTapSamples() {
        //Do nothing
    }

    public static MercadoPagoCheckout.Builder getMercadoPagoCheckoutBuilder() {
        final GenericPayment payment = new GenericPayment(123L, Payment.StatusCodes.STATUS_APPROVED,
                Payment.StatusDetail.STATUS_DETAIL_ACCREDITED);
        final PaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        excludedPaymentTypes.add("debit_card");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
                getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, checkoutPreferenceWithPayerEmail,
                PaymentConfigurationUtils.createWithPlugin(samplePaymentProcessor))
                .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setEscEnabled(true).build())
                .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN);
    }

    public static void addAll(final Collection<Pair<String, MercadoPagoCheckout.Builder>> options) {
        options.add(new Pair<>("4 - One tap - Should suggest credit card (excluded account money and debit)",
            startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoneyAndDebit()));
    }

    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoneyAndDebit() {
        final GenericPayment payment = new GenericPayment(123L, Payment.StatusCodes.STATUS_APPROVED,
            Payment.StatusDetail.STATUS_DETAIL_ACCREDITED);
        final PaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        excludedPaymentTypes.add("debit_card");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, checkoutPreferenceWithPayerEmail,
            PaymentConfigurationUtils.createWithPlugin(samplePaymentProcessor))
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setEscEnabled(true).build())
            .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN);
    }

    private static CheckoutPreference getCheckoutPreferenceWithPayerEmail(
            @NonNull final Collection<String> excludedPaymentTypes, final int amount) {
        final List<Item> items = new ArrayList<>();
        final Item item =
            new Item.Builder("Product title", 1, new BigDecimal(amount))
                .build();
        items.add(item);
        return new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();
    }
}
