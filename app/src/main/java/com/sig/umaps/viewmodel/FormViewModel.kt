package com.sig.umaps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sig.umaps.data.network.ApiConfig
import com.sig.umaps.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormViewModel : ViewModel() {

    private val _getData = MutableLiveData<RegisterResponse>()
    val getData: LiveData<RegisterResponse> = _getData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun putDataUser(
        accessToken: String?,
        userId: Int?,
        profilePic: String?,
        birthDate: String?,
        userStatus: String?
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiLogin(accessToken)
            .putRegisterData(userId, profilePic, birthDate, userStatus)
        client.enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(
                call: Call<RegisterResponse?>,
                response: Response<RegisterResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.d("TAG", "onFailure: ${t.message}")
            }

        })
    }

    fun getDataUser(accessToken: String?, userId: Int?) {
        _isLoading.value = true
        val client = ApiConfig.getApiLogin(accessToken).getDataUser(userId)
        client.enqueue(object : Callback<RegisterResponse?> {
            override fun onResponse(
                call: Call<RegisterResponse?>,
                response: Response<RegisterResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    _getData.value = response.body()
                } else {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }
}