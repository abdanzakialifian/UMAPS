package com.sig.umaps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sig.umaps.data.network.ApiConfig
import com.sig.umaps.model.FacilitiesResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val _data = MutableLiveData<FacilitiesResponseItem>()
    val data: LiveData<FacilitiesResponseItem> = _data

    fun getFacilityById(accessToken: String?, facilityId: Int?) {
        val client = ApiConfig.getApiLogin(accessToken).getFacilitiesById(facilityId)
        client.enqueue(object : Callback<FacilitiesResponseItem?> {
            override fun onResponse(
                call: Call<FacilitiesResponseItem?>,
                response: Response<FacilitiesResponseItem?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _data.value = response.body()
                } else {
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FacilitiesResponseItem?>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }
}