package com.snapmint.merchantsdk.api;


import com.snapmint.merchantsdk.models.EmiModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServices {

    @GET("assets/merchant/{path}")
    Call<EmiModel> getMerchantDetail(@Path("path") String path);

}
