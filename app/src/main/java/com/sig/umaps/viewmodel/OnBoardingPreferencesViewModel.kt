package com.sig.umaps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sig.umaps.helper.OnBoardingPreferences
import kotlinx.coroutines.launch

class OnBoardingPreferencesViewModel(private val pref: OnBoardingPreferences) : ViewModel() {
    fun getStarted(): LiveData<Boolean> {
        return pref.getStarted().asLiveData()
    }

    fun saveStarted(isStartedActive: Boolean) {
        viewModelScope.launch {
            pref.saveStarted(isStartedActive)
        }
    }
}