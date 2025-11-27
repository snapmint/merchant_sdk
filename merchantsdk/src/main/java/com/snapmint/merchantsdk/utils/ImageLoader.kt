package com.snapmint.merchantsdk.utils

import android.widget.ImageView
import coil.load

object ImageLoader {

    @JvmStatic
    fun load(imageView: ImageView, url: Any){
        imageView.load(url)
    }

}