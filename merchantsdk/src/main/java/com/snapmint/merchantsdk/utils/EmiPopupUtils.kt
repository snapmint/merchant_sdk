package com.snapmint.merchantsdk.utils

import android.content.Context
import com.snapmint.merchantsdk.models.EmiModel
import com.snapmint.merchantsdk.models.PopUpListItem

object EmiPopupUtils {

    @JvmStatic
    fun getEmiPopupListItem(context: Context, value: Double, model: EmiModel): PopUpListItem? {
        val configList = model.popUpList?.toMutableList() ?: mutableListOf()

        for (config in configList) {
            val min = config.min?.toDouble() ?: continue
            val max = config.max?.toDouble() ?: continue
            val maxValue = if (max == -1.0) Double.MAX_VALUE else max

            if (value >= min && value <= maxValue) {
                return config
            }
        }

        // Fallback if nothing matches
        return PopUpListItem(
            popup = loadHtmlFromAsset(context, "snapmint_popup_content.html"),
            emiPercentage = 0.0,
            payNowPercentage = 0.0
        )
    }



    private fun loadHtmlFromAsset(context: Context, fileName: String): String {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }
}

