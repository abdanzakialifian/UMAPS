package com.sig.umaps.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("user_status")
	val userStatus: Any? = null,

	@field:SerializedName("birth_date")
	val birthDate: Any? = null,

	@field:SerializedName("profile_pic")
	val profilePic: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user")
	val user: Int? = null
)
