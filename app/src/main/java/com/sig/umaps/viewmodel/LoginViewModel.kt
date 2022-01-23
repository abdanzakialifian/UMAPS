package com.sig.umaps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sig.umaps.data.network.ApiConfig
import com.sig.umaps.model.LoginGetResponse
import com.sig.umaps.model.LoginPostResponse
import com.sig.umaps.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> = _status

    private val _data = MutableLiveData<LoginPostResponse>()
    val data: LiveData<LoginPostResponse> = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun sendLoginData(loginRequest: LoginRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().sendLoginData(loginRequest)
        client.enqueue(object : Callback<LoginPostResponse?> {
            override fun onResponse(
                call: Call<LoginPostResponse?>,
                response: Response<LoginPostResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    _data.value = response.body()
                    val accessToken = response.body()?.accessToken
                    getLoginData(accessToken)
                } else {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginPostResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }

    fun getLoginData(accessToken: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiLogin(accessToken).getLoginData(accessToken)
        client.enqueue(object : Callback<LoginGetResponse?> {
            override fun onResponse(
                call: Call<LoginGetResponse?>,
                response: Response<LoginGetResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    _status.value = response.body()?.status
                } else {
                    _isLoading.value = false
                    Log.d("TAG", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginGetResponse?>, t: Throwable) {
                _isLoading.value = false
                Log.e("TAG", "onFailure: ${t.message}")
            }
        })
    }
}