package com.snapmint.merchantsdk.snapmintsdk;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;


import com.snapmint.merchantsdk.JSBridge.WebAppInterface;
import com.snapmint.merchantsdk.constants.ApiConstant;
import com.snapmint.merchantsdk.constants.SnapmintConstants;
import com.snapmint.merchantsdk.utils.WebViewClientImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView = null;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;
    public static final int REQUEST_SELECT_FILE = 100;

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

    private Uri imageUri;

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        //Extract parameters from intent and callback object and save

//        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

//        ActionBar toolbar = getSupportActionBar();

//        toolbar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setDisplayShowHomeEnabled(true);

        // Getting values from intent passed from merchant app
        Bundle bundle = getIntent().getExtras();

        if(bundle == null){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String PostData = bundle.getString(ApiConstant.DATA);
        String option_clicked = bundle.getString("option_clicked");

        ApiConstant.snapmintPaymentresponse = bundle.getParcelable(ApiConstant.MERCHANT_CALLBACK);

        webView = new WebView(MainActivity.this);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.setInitialScale(1);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        webView.addJavascriptInterface(new WebAppInterface(MainActivity.this), "Android");

        if(option_clicked == null){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (option_clicked.equals("check_out")) {
            if(PostData == null){
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Log.d("CheckoutUrl", SnapmintConstants.CHECKOUT_BASE_URL);
            webView.postUrl(SnapmintConstants.CHECKOUT_BASE_URL,PostData.getBytes());
        }

        setContentView(webView);

//        webView.postUrl(URL,PostData.getBytes());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(final WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

                final WebView newWebView = new WebView(MainActivity.this);

                System.out.println("Setting Layout Params");

                newWebView.getSettings().setJavaScriptEnabled(true);
                newWebView.getSettings().setSupportZoom(true);
                newWebView.getSettings().setBuiltInZoomControls(true);
                newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                newWebView.getSettings().setSupportMultipleWindows(true);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view1, String url) {
                        System.out.println("url:" + url);
                        view1.loadUrl(url);
                        if (url.contains("token_recurring_status")) {
                            System.out.println("remmove webview");
                            view.removeView(newWebView);

                        } else {
                            System.out.println("not remmove webview");
                        }
                        return true;
                    }
                });

                return true;
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), MainActivity.FILECHOOSER_RESULTCODE );

            }

            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
            {

                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
                // Create the storage directory if it does not exist
                if (! imageStorageDir.exists()){
                    imageStorageDir.mkdirs();
                }
                File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                imageUri = Uri.fromFile(file);

                final List<Intent> cameraIntents = new ArrayList<Intent>();
                final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                for(ResolveInfo res : listCam) {
                    final String packageName = res.activityInfo.packageName;
                    final Intent i = new Intent(captureIntent);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    i.setPackage(packageName);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    cameraIntents.add(i);

                }
                Log.v(this.getClass().getName(), "Sandip REQUEST_SELECT_FILE in 5.0+" + imageUri);

                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                //Used for opening File Manager in Chooser
//                i.setType("image/*");
//                Intent chooserIntent = Intent.createChooser(i,"Image Chooser");
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, i);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

                try
                {
                    MainActivity.this.startActivityForResult(chooserIntent,  REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(MainActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.finish();
//            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null) {
                    return;
                }

                Log.v(this.getClass().getName(), "Sandip Image Uri : " + this.imageUri);

                Intent i = new Intent();
                Uri result;
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    if (intent == null) {
                        try {
                            i.setData(this.imageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    intent = intent == null ? i
                        : intent; // retrieve from the private variable if the intent is null
                }

                uploadMessage.onReceiveValue(FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;

                Log.v(this.getClass().getName(), "Sandip Upload Image REQUEST_SELECT_FILE");
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result =
                intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

            Log.v(this.getClass().getName(), "Sandip Upload Image FILECHOOSER_RESULTCODE");

        } else {
            Toast.makeText(MainActivity.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
