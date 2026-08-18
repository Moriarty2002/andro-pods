package com.example

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppSettings(private val context: Context) {
    companion object {
        val ANC_ENABLED = booleanPreferencesKey("anc_enabled")
        val SPATIAL_AUDIO_ENABLED = booleanPreferencesKey("spatial_audio_enabled")
        val AUTO_PAUSE_ENABLED = booleanPreferencesKey("auto_pause_enabled")
    }

    val isAncEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ANC_ENABLED] ?: false
    }

    val isSpatialAudioEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SPATIAL_AUDIO_ENABLED] ?: false
    }

    val isAutoPauseEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_PAUSE_ENABLED] ?: false
    }

    suspend fun setAncEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ANC_ENABLED] = enabled
        }
        LogManager.log("ANC turned ${if (enabled) "ON" else "OFF"}")
    }

    suspend fun setSpatialAudioEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SPATIAL_AUDIO_ENABLED] = enabled
        }
        LogManager.log("Spatial Audio turned ${if (enabled) "ON" else "OFF"}")
    }

    suspend fun setAutoPauseEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_PAUSE_ENABLED] = enabled
        }
        LogManager.log("Auto Pause turned ${if (enabled) "ON" else "OFF"}")
    }
}
