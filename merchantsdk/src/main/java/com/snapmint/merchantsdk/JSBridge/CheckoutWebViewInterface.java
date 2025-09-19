package com.snapmint.merchantsdk.JSBridge;


import android.util.Log;
import android.webkit.JavascriptInterface;

import com.snapmint.merchantsdk.snapmintsdk.NewCheckoutWebViewActivity;

import org.jetbrains.annotations.Nullable;


public class CheckoutWebViewInterface {

  private static final String source = "MerchantAndroid";
//  private static final String source = "Android";
  private static NewCheckoutWebViewActivity mContext;

  public CheckoutWebViewInterface(NewCheckoutWebViewActivity mContext) {
    CheckoutWebViewInterface.mContext = mContext;
  }

  @JavascriptInterface
  public final void handlePaymentResponse(@Nullable String code, @Nullable String message) {
    mContext.handlePaymentResponse(code, message);
  }

  @JavascriptInterface
  public static String checkForNative() {
    Log.d("CheckoutWebView", "checkForNative: ");
    return source;
  }

  @JavascriptInterface
  public static void closeWebView() {
    Log.d("CheckoutWebView", "closeWebView: ");
    if (!mContext.isFinishing()) {
      mContext.orderFailed("");
    }
  }

  @JavascriptInterface
  public static void orderFailed(String data) {
    Log.d("CheckoutWebView", "orderFailed: " + data);
    if (!mContext.isFinishing()) {
      mContext.orderFailed(data);
    }
  }

  @JavascriptInterface
  public static void orderSuccess(String data) {
    Log.d("CheckoutWebView", "orderSuccess: " + data);
    if (!mContext.isFinishing()) {
      mContext.orderSuccess(data);
    }
  }

  interface OrderSuccessInterface {
      void orderSuccess(String data);
  }

}

