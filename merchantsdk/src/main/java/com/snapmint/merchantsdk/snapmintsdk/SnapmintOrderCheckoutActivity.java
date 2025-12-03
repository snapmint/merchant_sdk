package com.snapmint.merchantsdk.snapmintsdk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.snapmint.merchantsdk.R;
import com.snapmint.merchantsdk.constants.ApiConstant;
import com.snapmint.merchantsdk.constants.SnapmintConfiguration;
import com.snapmint.merchantsdk.constants.SnapmintConstants;
import com.snapmint.merchantsdk.databinding.ActivitySnapmintOrderCheckoutBinding;

import java.util.List;
import java.util.Set;

public class SnapmintOrderCheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckOutActivity";
    private ActivitySnapmintOrderCheckoutBinding binding;
    private SnapmintOrderCheckoutActivity mContext;
    private String PostData;
    private String optionClicked;
    private String confirmationUrl;
    private String failureUrl;
    private boolean isSuccess = false;
    private boolean isFailure = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnapmintOrderCheckoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mContext = this;
        initialisation();
    }

    private void initialisation() {
        getBundleData();
        setWebView();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        PostData = intent.getStringExtra(ApiConstant.DATA);
        optionClicked = intent.getStringExtra("option_clicked");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        if(optionClicked == null){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setUserAgentString("Android");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);
        binding.webView.setScrollbarFadingEnabled(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(0);
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setWebViewClient(new webClient());
        binding.webView.setWebChromeClient(new webChromeClient());
        if (optionClicked.equals("check_out")) {
            if(PostData == null){
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Log.d("CheckoutUrl", SnapmintConstants.CHECKOUT_BASE_URL);
            binding.webView.postUrl(SnapmintConstants.CHECKOUT_BASE_URL, PostData.getBytes());
        }
        try {
            Uri uri = Uri.parse(SnapmintConstants.CHECKOUT_BASE_URL + "/" + PostData);
            String server = uri.getAuthority();
            String path = uri.getPath();
            String protocol = uri.getScheme();
            Set<String> args = uri.getQueryParameterNames();
            confirmationUrl = uri.getQueryParameter("merchant_confirmation_url");  //will return "V-Maths-Addition "
            failureUrl = uri.getQueryParameter("merchant_failure_url");
        } catch (Exception ignored) {

        }
        binding.webView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                WebView webView = (WebView) v;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
            }
            return true;
        });
    }

    public class webClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (url == null || url.startsWith("http://") || url.startsWith("https://")) {
                    if(url != null){
                        view.loadUrl(url);
                    }
                    return false;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                } catch (Exception e) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            String webUrl = view.getUrl();
            if(webUrl==null) return;
            if (webUrl.equalsIgnoreCase(confirmationUrl)) {
                if (!isSuccess) {
                    isSuccess = true;
                    Intent intent = new Intent();
                    intent.putExtra(SnapmintConfiguration.STATUS, SnapmintConfiguration.SUCCESS);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (webUrl.equalsIgnoreCase(failureUrl)) {
                if (!isFailure) {
                    isFailure = true;
                    Intent intent = new Intent();
                    intent.putExtra(SnapmintConfiguration.STATUS, SnapmintConfiguration.FAILED);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", (dialog, which) -> handler.proceed());
            builder.setNegativeButton("cancel", (dialog, which) -> handler.cancel());
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            view.loadUrl("javascript:document.querySelectorAll('.snap-nav').forEach(function(a){\n" +
                    "a.remove()\n" +
                    "});" +
                    "document.querySelectorAll('.mob-footer-new').forEach(function(a){\n" +
                    "a.remove()\n" +
                    "});" +
                    "document.querySelectorAll('.footer').forEach(function(a){\n" +
                    "a.remove()\n" +
                    "});" +
                    "document.body.style.paddingTop='0'; void 0");
        }
    }

    public class webChromeClient extends WebChromeClient {
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            try {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    TedPermission.create()
                            .setPermissionListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted() {
                                    request.grant(request.getResources());
                                }

                                @Override
                                public void onPermissionDenied(List<String> deniedPermissions) {
                                    Toast.makeText(mContext, R.string.please_give_camera_permission_without_that_we_cannot, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setDeniedMessage(R.string.please_give_camera_permission_without_that_we_cannot)
                            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .check();
                } else {
                    request.grant(request.getResources());
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            String webUrl = view.getUrl();
            if(webUrl==null)return;
            if (webUrl.equalsIgnoreCase(confirmationUrl)) {
                if (!isSuccess) {
                    isSuccess = true;
                    Intent intent = new Intent();
                    intent.putExtra(SnapmintConfiguration.STATUS, SnapmintConfiguration.SUCCESS);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (webUrl.equalsIgnoreCase(failureUrl)) {
                if (!isFailure) {
                    isFailure = true;
                    Intent intent = new Intent();
                    intent.putExtra(SnapmintConfiguration.STATUS, SnapmintConfiguration.FAILED);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}