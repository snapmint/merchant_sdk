package com.snapmint.merchantsdk.utils;

import org.jetbrains.annotations.Nullable;

public interface CheckoutResponse {
    void handlePaymentResponse(@Nullable String var1, @Nullable String var2);
    void orderSuccess(String data);
    void orderFailed(String data);
}
