package com.sig.umaps.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnBoardingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val startedKey = booleanPreferencesKey("started")

    fun getStarted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[startedKey] ?: false
        }
    }

    suspend fun saveStarted(isStartedActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[startedKey] = isStartedActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OnBoardingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): OnBoardingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = OnBoardingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}