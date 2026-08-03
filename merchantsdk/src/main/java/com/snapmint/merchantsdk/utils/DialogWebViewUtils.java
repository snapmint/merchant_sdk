package com.snapmint.merchantsdk.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;

public final class DialogWebViewUtils {
    private static final int MAX_DIALOG_MEASURE_ATTEMPTS = 5;
    private static final long INITIAL_DIALOG_MEASURE_DELAY_MS = 120L;
    private static final long MAX_DIALOG_MEASURE_DELAY_MS = 1920L;

    private DialogWebViewUtils() {
    }

    public static void resizeDialogWebView(WebView popupWebView, Dialog dialog, String logTag, Runnable onComplete) {
        resizeDialogWebView(popupWebView, dialog, logTag, 0, onComplete);
    }

    private static void resizeDialogWebView(WebView popupWebView, Dialog dialog, String logTag, int attempt, Runnable onComplete) {
        long delayMs = Math.min(INITIAL_DIALOG_MEASURE_DELAY_MS << attempt, MAX_DIALOG_MEASURE_DELAY_MS);
        popupWebView.postDelayed(() -> popupWebView.evaluateJavascript(
                "(function(){" +
                        "var modal=document.querySelector('.modal-wrpr');" +
                        "var body=document.body;" +
                        "var doc=document.documentElement;" +
                        "var modalHeight=modal?Math.max(modal.scrollHeight,modal.offsetHeight,modal.getBoundingClientRect().height):0;" +
                        "return Math.max(modalHeight,body.scrollHeight,body.offsetHeight,doc.scrollHeight,doc.offsetHeight,doc.clientHeight);" +
                        "})()",
                value -> {
                    DisplayMetrics displayMetrics = popupWebView.getResources().getDisplayMetrics();
                    int minHeightPx = (int) (320 * displayMetrics.density);
                    int maxHeightPx = (int) (displayMetrics.heightPixels * 0.85f);
                    int jsHeightPx = 0;

                    try {
                        if (!TextUtils.isEmpty(value) && !"null".equals(value)) {
                            String sanitizedValue = value.replace("\"", "");
                            double cssHeight = Double.parseDouble(sanitizedValue);
                            jsHeightPx = (int) Math.ceil(cssHeight * displayMetrics.density);
                        }
                    } catch (NumberFormatException exception) {
                        Log.w(logTag, "Unable to parse popup height", exception);
                    }

                    int webViewContentHeightPx = (int) Math.ceil(popupWebView.getContentHeight() * displayMetrics.density);
                    int measuredHeightPx = Math.max(jsHeightPx, webViewContentHeightPx);
                    int targetHeightPx = measuredHeightPx > 0 ? Math.max(measuredHeightPx, minHeightPx) : minHeightPx;
                    targetHeightPx = Math.min(targetHeightPx, maxHeightPx);

                    boolean shouldRetry = measuredHeightPx <= 0 && attempt < MAX_DIALOG_MEASURE_ATTEMPTS - 1;
                    if (shouldRetry) {
                        resizeDialogWebView(popupWebView, dialog, logTag, attempt + 1, onComplete);
                        return;
                    }

                    ViewGroup.LayoutParams layoutParams = popupWebView.getLayoutParams();
                    if (layoutParams != null && layoutParams.height != targetHeightPx) {
                        layoutParams.height = targetHeightPx;
                        popupWebView.setLayoutParams(layoutParams);
                        popupWebView.requestLayout();
                    }

                    Window window = dialog.getWindow();
                    if (window != null) {
                        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }

                    if (onComplete != null) {
                        onComplete.run();
                    }
                }), delayMs);
    }

    public static void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);
    }

    public static void fadeOut(View view, Dialog dialog) {
        view.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                        dialog.dismiss();
                    }
                });
    }
}

