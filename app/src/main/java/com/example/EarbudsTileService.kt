package com.example

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EarbudsTileService : TileService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var appSettings: AppSettings

    override fun onCreate() {
        super.onCreate()
        appSettings = AppSettings(applicationContext)
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    private fun updateTileState() {
        serviceScope.launch {
            val isAutoPauseEnabled = appSettings.isAutoPauseEnabled.first()
            val tile = qsTile
            if (tile != null) {
                tile.state = if (isAutoPauseEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                tile.label = "Auto Pause"
                tile.subtitle = if (isAutoPauseEnabled) "On" else "Off"
                tile.updateTile()
            }
        }
    }

    override fun onClick() {
        super.onClick()
        serviceScope.launch {
            val isAutoPauseEnabled = appSettings.isAutoPauseEnabled.first()
            val newState = !isAutoPauseEnabled
            appSettings.setAutoPauseEnabled(newState)
            
            val tile = qsTile
            if (tile != null) {
                tile.state = if (newState) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                tile.subtitle = if (newState) "On" else "Off"
                tile.updateTile()
            }
            LogManager.log("Quick Settings Tile: Auto Pause toggled ${if (newState) "ON" else "OFF"}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
