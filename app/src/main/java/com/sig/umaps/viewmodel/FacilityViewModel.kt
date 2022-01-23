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

class FacilityViewModel : ViewModel() {
    private val _data = MutableLiveData<ArrayList<FacilitiesResponseItem>>()
    val data: LiveData<ArrayList<FacilitiesResponseItem>> = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFacility(accessToken: String?, type: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiLogin(accessToken).getFacilities(type)
        client.enqueue(object : Callback<ArrayList<FacilitiesResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<FacilitiesResponseItem>>,
                response: Response<ArrayList<FacilitiesResponseItem>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    _data.value = response.body()
                } else {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<FacilitiesResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }
}