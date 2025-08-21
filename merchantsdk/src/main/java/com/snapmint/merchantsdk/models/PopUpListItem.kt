package com.snapmint.merchantsdk.models

import android.health.connect.datatypes.units.Percentage
import com.google.gson.annotations.SerializedName

data class PopUpListItem(

	@field:SerializedName("popup")
	val popup: String? = null,

	@field:SerializedName("pay_now_percentage")
	val payNowPercentage: Double? = null,

	@field:SerializedName("emi_percentage")
	val emiPercentage: Double? = null,

	@field:SerializedName("min")
	val min: Int? = null,

	@field:SerializedName("max")
	val max: Int? = null
)
