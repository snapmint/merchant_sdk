package com.snapmint.merchantsdk.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.snapmint.merchantsdk.R;
import com.snapmint.merchantsdk.api.ApiBuilder;
import com.snapmint.merchantsdk.api.ApiServices;
import com.snapmint.merchantsdk.models.EmiModel;
import com.snapmint.merchantsdk.models.PopUpListItem;
import com.snapmint.merchantsdk.models.TenureModel;
import com.snapmint.merchantsdk.utils.EmiPopupUtils;
import com.snapmint.merchantsdk.utils.Utility;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SnapmintEmiInfoButton extends FrameLayout implements View.OnClickListener {
    private WebView emiWebView;
    private TextView tvPayment;
    private TextView tvCredit;
    private ImageView ivSnapmint;
    private ImageView ivSnapmintLogo;
    private ImageView ivSnapmintText;
    private ImageView ivReadMore;
    private Double amountPay;
    private View view;
    private TextView tvPaymentText2;
    private TextView tvPaymentText3;
    private TextView tvPaymentText4;
    private TextView tvDisableText1;
    private TextView tvCashbackUpTo;
    private TextView tvDisableText4;
    private TextView tvFlatOffer;
    private TextView tvDisableText6;
    private TextView tvDisableText8;
    private String orderValue;
    private String merchantLink;
    private boolean isEnable;
    private Double firstEmiAmount;
    private Double secondEmiAmount;
    private Double thirdEmiAmount;
    private Double amountPayDisabled;
    private EmiModel model = new EmiModel();
    private Context mContext;
    private WebView webView;
    private PopUpListItem popupItem;

    public SnapmintEmiInfoButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SnapmintEmiInfoButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnapmintEmiInfoButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void showSnapmintEmiInfo(String orderValue, String merchantLink) {
        this.orderValue = orderValue;
        this.merchantLink = merchantLink;
        getEmiInfo();

    }

    private void init(Context context) {
        mContext = context;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view = LayoutInflater.from(context).inflate(R.layout.snapmint_info_layout, this, true);
        tvPayment = findViewById(R.id.tvPayment);
        tvPaymentText2 = findViewById(R.id.tvPaymentText2);
        tvPaymentText3 = findViewById(R.id.tvPaymentText3);
        tvPaymentText4 = findViewById(R.id.tvPaymentText4);
        tvDisableText1 = findViewById(R.id.tvDisableText1);
        tvCashbackUpTo = findViewById(R.id.tvCashbackUpTo);
        tvDisableText4 = findViewById(R.id.tvDisableText4);
        tvFlatOffer = findViewById(R.id.tvFlatOffer);
        tvDisableText6 = findViewById(R.id.tvDisableText6);
        tvDisableText8 = findViewById(R.id.tvDisableText8);
        tvCredit = findViewById(R.id.tvCredit);
        emiWebView = findViewById(R.id.emiWebView);
        TextView tvTnc = findViewById(R.id.tvTnc);
        ivSnapmint = findViewById(R.id.ivSnapmint);
        ivSnapmintLogo = findViewById(R.id.ivSnapmintLogo);
        ivSnapmintText = findViewById(R.id.ivSnapmintText);
        ivReadMore = findViewById(R.id.ivReadMore);
        LinearLayout llEnableView = findViewById(R.id.llEnableView);
        setOnClickListener(this);
        llEnableView.setOnClickListener(this);
        tvTnc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llEnableView) {
            openSnapmintDialog(!TextUtils.isEmpty(model.getAvailableOffer()) && !TextUtils.isEmpty(model.getOfferPercentage()), false);
        } else if (view.getId() == R.id.tvTnc) {
            openSnapmintDialog(!TextUtils.isEmpty(model.getAvailableOffer()) && !TextUtils.isEmpty(model.getOfferPercentage()), true);
        }
    }

    private String loadHtmlFromAsset(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint("SetJavaScriptEnabled,SimpleDateFormat")
    private void openSnapmintDialog(boolean isOffer, boolean isTAnC) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_snapmint_html_web_view);

        webView = dialog.findViewById(R.id.webView);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String nextMonth = "";
        String secondMonth = "";
        String thirdMonth = "";
        int nextMonthDay = 0;
        int secondMonthDay = 0;
        int thirdMonthDay = 0;
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        try {
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            Calendar cal3 = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Calculate next month
            cal.add(Calendar.MONTH, day > 23 ? 2 : 1);
            nextMonth = monthFormat.format(cal.getTime());
            nextMonthDay = Integer.parseInt(dayFormat.format(cal.getTime()));

            // Calculate the month after next
            cal2.add(Calendar.MONTH, day > 23 ? 3 : 2);
            secondMonth = monthFormat.format(cal2.getTime());
            secondMonthDay = Integer.parseInt(dayFormat.format(cal2.getTime()));

            // Calculate the month after next
            cal3.add(Calendar.MONTH, day > 23 ? 4 : 3);
            thirdMonth = monthFormat.format(cal3.getTime());
            thirdMonthDay = Integer.parseInt(dayFormat.format(cal3.getTime()));

        } catch (Exception ignored) {
        }

        if (webView != null) {
            webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
            WebSettings webSettings = webView.getSettings();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setDomStorageEnabled(true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            String htmlContent = popupItem.getPopup();
            if(TextUtils.isEmpty(htmlContent)) return;
            htmlContent = htmlContent.replace("{{down_payment_price}}", String.valueOf(amountPay.intValue()));
            htmlContent = htmlContent.replace("{{pay_now_price}}", String.valueOf(amountPay.intValue()));
            htmlContent = htmlContent.replace("{{first_emi_date}}", String.valueOf(nextMonthDay));
            htmlContent = htmlContent.replace("{{first_emi_month}}", nextMonth);
            htmlContent = htmlContent.replace("{{second_emi_date}}", String.valueOf(secondMonthDay));
            htmlContent = htmlContent.replace("{{second_emi_month}}", secondMonth);
            htmlContent = htmlContent.replace("{{third_emi_date}}", String.valueOf(thirdMonthDay));
            htmlContent = htmlContent.replace("{{third_emi_month}}", thirdMonth);
            htmlContent = htmlContent.replace("{{first_emi_price}}", String.valueOf(firstEmiAmount.intValue()));
            htmlContent = htmlContent.replace("{{second_emi_price}}", String.valueOf(secondEmiAmount.intValue()));
            htmlContent = htmlContent.replace("{{third_emi_price}}", String.valueOf(thirdEmiAmount.intValue()));
            htmlContent = htmlContent.replace("{{first_emi_suffix}}", Utility.getNumberSuffix(nextMonthDay));
            htmlContent = htmlContent.replace("{{second_emi_suffix}}", Utility.getNumberSuffix(secondMonthDay));
            htmlContent = htmlContent.replace("{{third_emi_suffix}}", Utility.getNumberSuffix(thirdMonthDay));
            htmlContent = htmlContent.replace("{{total_order_value}}", orderValue);
            htmlContent = htmlContent.replace("http://", "https://");
            if (model.getTenureList() != null && !model.getTenureList().isEmpty()) {
                for (int i = 0; i < model.getTenureList().size(); i++) {
                    TenureModel tenureModel = model.getTenureList().get(i);
                    double tenureValue;
                    if (tenureModel.getRoi() > 0) {
                        tenureValue = (Double.parseDouble(orderValue) * tenureModel.getRoi()) / 100;
                    } else {
                        tenureValue = (Double.parseDouble(orderValue) - amountPay) / tenureModel.getTenure();
                    }
                    String[] tenureList = String.valueOf(tenureValue).split("\\.");
                    if (tenureList.length > 1) {
                        double pointValue = Double.parseDouble(tenureList[1]);
                        if (pointValue > 0) {
                            tenureValue = Double.parseDouble(tenureList[0]) + 1;
                        }
                    }
                    htmlContent = htmlContent.replace("{{tenure_" + tenureModel.getTenure() + "}}", String.valueOf(Math.round(tenureValue)));
                }
            }

            webView.addJavascriptInterface(new MyWebJavaInterFace(dialog), "Android");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                    fadeIn(webView);
                }
            });
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);

        }
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // Optional for a transparent background
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.CENTER; // Center the dialog
            window.setAttributes(params);
            window.setDimAmount(0.5f);// Apply the animation style
        }
        dialog.show();

    }

    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(300) // Animation duration in milliseconds
                .setListener(null);
    }

    private void fadeOut(View view, Dialog mDialog) {
        view.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                        mDialog.dismiss();
                    }
                });
    }

    public class MyWebJavaInterFace extends AppCompatActivity {
        private final Dialog mDialog;

        MyWebJavaInterFace(Dialog dialog) {
            mDialog = dialog;
        }

        @JavascriptInterface
        public void closePopup() {
            runOnUiThread(() -> {
                if (mDialog != null && mDialog.isShowing()) {
                    fadeOut(webView, mDialog);
                }
            });
        }
    }

    private void getEmiInfo() {
        double totalOrder = Double.parseDouble(orderValue);
        ApiServices retrofitAPI = ApiBuilder.create(ApiServices.class);
        Call<EmiModel> call = retrofitAPI.getMerchantDetail(merchantLink);
        call.enqueue(new Callback<EmiModel>() {
            @Override
            public void onResponse(@NonNull Call<EmiModel> call, @NonNull Response<EmiModel> response) {
                try {
                    model = response.body();
                    if (model != null) {
                       popupItem = EmiPopupUtils.getEmiPopupListItem(mContext,totalOrder,model);

                        if (popupItem!=null) {
                            Double payNowPercentage = popupItem.getPayNowPercentage();
                            Double emiPercentage = popupItem.getEmiPercentage();
                            if (payNowPercentage!=null) {
                                amountPay = (totalOrder * payNowPercentage / 100);
                            }
                            double input = Math.floor(amountPay);
                            double afterDecimal = amountPay - input;
                            if (afterDecimal > 0) {
                                amountPay = amountPay + 1;
                            }
                            if(emiPercentage!=null){
                                firstEmiAmount =(totalOrder * emiPercentage) / 100;
                                double inputEmi = Math.floor(firstEmiAmount);
                                double afterDecimalEmi = firstEmiAmount - inputEmi;
                                if (afterDecimalEmi > 0) {
                                    firstEmiAmount = firstEmiAmount + 1;
                                }

                                secondEmiAmount = (totalOrder * emiPercentage) / 100;
                                thirdEmiAmount = (totalOrder * emiPercentage)/ 100;
                                double secInput = Math.floor(secondEmiAmount);
                                double thirdInput = Math.floor(thirdEmiAmount);
                                double secAfterDecimal = secondEmiAmount - secInput;
                                double thirdAfterDecimal = thirdEmiAmount - thirdInput;

                                if (secAfterDecimal > 0) {
                                    secondEmiAmount = secondEmiAmount + 1;
                                }
                                if (thirdAfterDecimal > 0) {
                                    thirdEmiAmount = thirdEmiAmount + 1;
                                }
                                double emiDisabledAmount = (totalOrder * Double.parseDouble(model.getEmiRatesPercentagePopUpDisable()) / 100);
                                double emiDesInput = Math.floor(emiDisabledAmount);
                                double secDesAfterDecimal = emiDisabledAmount - emiDesInput;
                                if (secDesAfterDecimal > 0) {
                                    emiDisabledAmount = emiDisabledAmount + 1;
                                }
                                setEmiWebView(model.getEmiWidget());

                                tvPayment.setText(model.getPayNowText1Part1());
                                tvPaymentText2.setText(model.getPayNowText1Part2().replace("pay_now", Utility.setSingleDynamicValue(view.getContext(), R.string.rs_amount, String.valueOf(amountPay.intValue()))));
                                tvPaymentText3.setText(model.getPayNowText1Part3());
                                tvPaymentText4.setText(model.getPayNowText1Part4());
                                tvDisableText1.setText(model.getPayNowText1PopUpDisable());
                                tvDisableText4.setText(model.getPayNowText2PopUpDisable().replace("pay_now", Utility.setSingleDynamicValue(view.getContext(), R.string.rs_amount, String.valueOf(amountPayDisabled.intValue()))));
                                tvDisableText6.setText(model.getPayNowText3PopUpDisable().replace("emi_rate", String.valueOf((int) emiDisabledAmount)));
                                tvDisableText8.setText(model.getPayNowText4PopUpDisable());
                                Glide.with(view.getContext()).load(model.getPayNowText2()).into(ivSnapmint);
                                Glide.with(view.getContext()).load(model.getPayNowImagePopUpDisable()).into(ivSnapmintLogo);
                                Glide.with(view.getContext()).load(model.getPayNowImage1PopUpDisable()).into(ivSnapmintText);
                                Glide.with(view.getContext()).load("https://assets.snapmint.com/assets/merchant/emitxt/green_dark_button.png").into(ivReadMore);
                                tvCredit.setText(model.getPayNowText3());
                                if (!TextUtils.isEmpty(model.getOfferPercentage()) && !TextUtils.isEmpty(model.getAvailableOffer())) {
                                    tvFlatOffer.setText(model.getOfferPercentage());
                                    ivReadMore.setVisibility(GONE);
                                    tvCashbackUpTo.setText(model.getAvailableOffer().replace("T&C", ""));
                                } else {
                                    ivReadMore.setVisibility(VISIBLE);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e("TAG", "onFailure: " + e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmiModel> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t);
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void setEmiWebView(String emiWidget) {
        WebSettings webSettings = emiWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setLoadsImagesAutomatically(true);
        if (TextUtils.isEmpty(emiWidget)) {
            emiWidget = loadHtmlFromAsset(mContext, "snap_emi_widget.html");
        }
        // Replace placeholder with amountPay value
        emiWidget = emiWidget.replace("{{down_payment_price}}", String.valueOf(amountPay.intValue()));
        emiWidget = emiWidget.replace("{{pay_now_price}}", String.valueOf(amountPay.intValue()));

        // Set a WebViewClient to handle page loading events
        emiWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("WebView", "Error loading webpage: " + description);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebView", "Page finished loading: " + url);
            }
        });
        String modifiedHtml = " <html>" +
                "<head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "</head>" +
                "<body> " +
                emiWidget +
                "</body>" +
                "</html>";

        emiWebView.loadDataWithBaseURL(null, modifiedHtml, "text/html", "UTF-8", null);
        emiWebView.setOnTouchListener(new OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();
                        float touchSlop = ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
                        if (Math.abs(endX - startX) < touchSlop && Math.abs(endY - startY) < touchSlop) {
                            openSnapmintDialog(!TextUtils.isEmpty(model.getAvailableOffer()) && !TextUtils.isEmpty(model.getOfferPercentage()), false);
                            return true; // Consume the touch event
                        }
                        break;
                }
                return false;
            }
        });


    }
}
