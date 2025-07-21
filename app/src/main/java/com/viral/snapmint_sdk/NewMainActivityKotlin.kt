package com.viral.snapmint_sdk

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.snapmint.merchantsdk.api.CurlLoggerInterceptor
import com.snapmint.merchantsdk.constants.SnapmintConfiguration
import com.snapmint.merchantsdk.snapmintsdk.NewCheckoutWebViewActivity
import com.viral.snapmint_sdk.databinding.ActivityNewMainKotlinBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.Random
import java.util.concurrent.TimeUnit

class NewMainActivityKotlin : AppCompatActivity() {
    private lateinit var binding: ActivityNewMainKotlinBinding
    private lateinit var mContext: NewMainActivityKotlin
    private var progressBar: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false)
        binding = ActivityNewMainKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this
        initView()
    }

    private fun initView() {
        setUiData()
        initCLickListener()
    }

    private fun initCLickListener() = binding.apply {
        btnChangeOrderValue.setOnClickListener {
            titanInfoButton.showSnapmintEmiInfo(etOrderValue.text.toString(), "4858/snap_titan.json")
        }
        btnCheckOut.setOnClickListener {
            if (!isDataValidate()) return@setOnClickListener
            callOkHttpApi(etBaseUrl.text?.trim().toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUiData() = binding.apply {
        titanInfoButton.showSnapmintEmiInfo("4500", "4858/snap_titan.json")
        etOrderValue.setText("1935")
        etOrderValueCheckout.setText("1935")
        etMobile.setText("8152041105")
//        etMerchantId.setText("2459")
        etMerchantId.setText("1456")
        etMerchantPassword.setText("UOYY0R_n")
//        etMerchantPassword.setText("DG9i2c_P")
        etFirstName.setText("Manish")
        etLastName.setText("T")
        etMerchantOrderId.setText("MELORRA-" + Random().nextInt())
        etMerchantSuccessUrl.setText("https://sf-sit-app.titan.co.in/payments/snapmint/success")
        etMerchantFailureUrl.setText("https://sf-sit-app.titan.co.in/payments/snapmint/failure")
//        etBaseUrl.setText("https://apis.qa.snapmint.com/api/pub/carts")
        etBaseUrl.setText("https://pay.sandbox.snapmint.com/api/pub/carts")
        etSku.setText("abdx123")
        etUnitPrice.setText("1000")
        etQuantity.setText("5")
    }

    private fun getNewJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("otpBypass", binding.switchOtp.isChecked)
            jsonObject.put("ip", "127.0.0.1")
            jsonObject.put("merchantPassword", binding.etMerchantPassword.getText().toString().trim { it <= ' ' })
            jsonObject.put("merchantId", binding.etMerchantId.getText().toString().trim { it <= ' ' })
            jsonObject.put("merchantConfirmationUrl", binding.etMerchantSuccessUrl.getText().toString().trim { it <= ' ' })
            jsonObject.put("merchantFailureUrl", binding.etMerchantFailureUrl.getText().toString().trim { it <= ' ' })
            jsonObject.put("mobile", binding.etMobile.getText().toString().trim { it <= ' ' })
            jsonObject.put("merchantOrderId", binding.etMerchantOrderId.getText().toString().trim { it <= ' ' })
            jsonObject.put("orderValue", binding.etOrderValueCheckout.getText().toString().trim { it <= ' ' })
            jsonObject.put("udf1", "1.91")
            jsonObject.put("udf2", "7147")
            jsonObject.put("first_name", binding.etFirstName.getText().toString().trim { it <= ' ' })
            jsonObject.put("last_name", binding.etLastName.getText().toString().trim { it <= ' ' })
            jsonObject.put("deviceType", "android")
            val product = JSONObject()
            product.put("sku", binding.etSku.getText().toString().trim { it <= ' ' })
            product.put("name", "Bold Show Diamond Earrings")
            product.put("quantity", binding.etQuantity.getText().toString().trim { it <= ' ' })
            product.put("unitPrice", binding.etUnitPrice.getText().toString().trim { it <= ' ' })
            product.put("itemUrl", "https://example.com/product1")
            product.put("imageUrl", "https://example.com/product1.jpg")
            product.put("udf2", "7147")
            product.put("udf1", "1.910 g")
            product.put("udf3", "feature")

            val productJsonArray = JSONArray()
            productJsonArray.put(product)
            jsonObject.put("products", productJsonArray)
        } catch (e: Exception) {
            Log.e("TAG", "getNewJsonObject: " + e.message)
        }
        return jsonObject
    }

    private fun isDataValidate(): Boolean {
        binding.apply {
            if (TextUtils.isEmpty(etMobile.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etMerchantId.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etBaseUrl.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etMerchantPassword.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etMerchantOrderId.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etOrderValueCheckout.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etMerchantSuccessUrl.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etMerchantFailureUrl.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etFirstName.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etSku.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etUnitPrice.getText().toString().trim { it <= ' ' })
                || TextUtils.isEmpty(etQuantity.getText().toString().trim { it <= ' ' })
                || etQuantity.getText().toString().trim { it <= '0' }.toInt() <= 0
            ) {
                return false
            }
        }

        return true
    }

    private fun callOkHttpApi(baseUrl: String) {
        showProgress(this)

        val json = getNewJsonObject().toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toRequestBody(mediaType)

        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(120, TimeUnit.SECONDS)    // Server response timeout
            .writeTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(CurlLoggerInterceptor("cURL"))
        }

        val client = clientBuilder.build()

        val request = Request.Builder()
            .url(baseUrl)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    dismissProgress()
                    showErrorDialog(e.message ?: "Network error")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val bodyString = response.body?.string() ?: throw IOException("Empty response")
                    val jsonObject = JSONObject(bodyString)

                    runOnUiThread {
                        dismissProgress()
                        if (jsonObject.has("url")) {
                            if (!isFinishing) {
                                val intent = Intent(mContext, NewCheckoutWebViewActivity::class.java).apply {
                                    putExtra("redirect_url", jsonObject.getString("url"))
                                }
                                snapmintLauncher.launch(intent)
                                // startActivityForResult is deprecated; use only snapmintLauncher
                            }
                        } else {
                            val message = jsonObject.optString("message")
                                .takeIf { it.isNotEmpty() }
                                ?: jsonObject.optString("code", "Something went wrong")
                            showErrorDialog(message)
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Log.e("NewCheckout", "callOkHttpApi: $e")
                        dismissProgress()
                        showErrorDialog("Incomplete response received from application")
                    }
                }
            }
        })
    }

    private val snapmintLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data
            val status = data?.getStringExtra(SnapmintConfiguration.STATUS)
            if (SnapmintConfiguration.SUCCESS.equals(status, ignoreCase = true)) {
                Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showErrorDialog(message: String?) {
        runOnUiThread(Runnable {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage(message)
            builder.setTitle("Error !")
            builder.setNegativeButton("Ok", DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                dialog?.cancel()
            }

            )
            val alertDialog = builder.create()
            alertDialog.show()
        })
    }

    fun showProgress(context: Context?) {
        try {
            if (progressBar != null) {
                if (progressBar!!.isShowing) {
                    progressBar!!.dismiss()
                }
                progressBar = null
            }
            progressBar = ProgressDialog(context)
            progressBar?.setCancelable(true)
            progressBar?.setMessage("Please wait ...")
            progressBar?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressBar?.progress = 0
            progressBar?.max = 100
            progressBar?.setCancelable(false)
            progressBar?.setCanceledOnTouchOutside(false)
            progressBar?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissProgress() {
        try {
            if (progressBar != null && progressBar!!.isShowing) {
                progressBar?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}