package com.sig.umaps.data.network

import com.sig.umaps.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("google/login/")
    fun sendLoginData(
        @Body loginRequest: LoginRequest
    ): Call<LoginPostResponse>

    @GET("google/")
    fun getLoginData(
        @Query("accessToken") accessToken: String?
    ): Call<LoginGetResponse>

    @FormUrlEncoded
    @PUT("api/profile/{user-id}/")
    fun putRegisterData(
        @Path("user-id") userId: Int?,
        @Field("profile_pic") profilePic: String?,
        @Field("birth_date") birthDate: String?,
        @Field("user_status") userStatus: String?
    ): Call<RegisterResponse>

    @GET("/api/profile/{user-id}/")
    fun getDataUser(
        @Path("user-id") userId: Int?
    ): Call<RegisterResponse>

    @GET("api/facilities/")
    fun getFacilities(
        @Query("type") type: String
    ): Call<ArrayList<FacilitiesResponseItem>>

    @GET("api/facilities/{facility-id}/")
    fun getFacilitiesById(
        @Path("facility-id") facilityId: Int?
    ): Call<FacilitiesResponseItem>
}