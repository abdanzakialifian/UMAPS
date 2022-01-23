package com.sig.umaps.model

import com.google.gson.annotations.SerializedName

data class FacilitiesResponseItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("rate")
	val rate: String? = null,

	@field:SerializedName("portrait_image_url")
	val portraitImageUrl: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("landscape_image_url")
	val landscapeImageUrl: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
