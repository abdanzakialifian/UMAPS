package com.sig.umaps.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sig.umaps.viewmodel.OnBoardingPreferencesViewModel

class OnBoardingViewModelFactory(private val pref: OnBoardingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnBoardingPreferencesViewModel::class.java)) {
            return OnBoardingPreferencesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name}")
    }
}