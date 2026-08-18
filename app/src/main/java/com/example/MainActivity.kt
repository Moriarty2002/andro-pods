package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.BluetoothAudio
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appSettings = AppSettings(this)

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AirPodsControlScreen(
                        modifier = Modifier.padding(innerPadding),
                        appSettings = appSettings
                    )
                }
            }
        }
    }
}

@Composable
fun AirPodsControlScreen(modifier: Modifier = Modifier, appSettings: AppSettings) {
    val coroutineScope = rememberCoroutineScope()
    
    val isAncEnabled by appSettings.isAncEnabled.collectAsStateWithLifecycle(initialValue = false)
    val isSpatialAudioEnabled by appSettings.isSpatialAudioEnabled.collectAsStateWithLifecycle(initialValue = false)
    val isAutoPauseEnabled by appSettings.isAutoPauseEnabled.collectAsStateWithLifecycle(initialValue = false)
    
    val logs by LogManager.logs.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Earbuds Control",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp, top = 24.dp)
        )

        FeatureToggle(
            title = "Active Noise Cancellation",
            subtitle = "Block out external noise",
            icon = Icons.Filled.Block,
            isChecked = isAncEnabled,
            onCheckedChange = { 
                coroutineScope.launch { appSettings.setAncEnabled(it) } 
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FeatureToggle(
            title = "Spatial Audio",
            subtitle = "Immersive 3D sound",
            icon = Icons.Filled.BluetoothAudio,
            isChecked = isSpatialAudioEnabled,
            onCheckedChange = { 
                coroutineScope.launch { appSettings.setSpatialAudioEnabled(it) } 
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FeatureToggle(
            title = "Auto Pause",
            subtitle = "Stop music when removed from ear",
            icon = Icons.Filled.PauseCircle,
            isChecked = isAutoPauseEnabled,
            onCheckedChange = { 
                coroutineScope.launch { appSettings.setAutoPauseEnabled(it) } 
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "System Logs",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            if (logs.isEmpty()) {
                Text(
                    text = "No logs yet...",
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(logs) { log ->
                        Text(
                            text = log,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 4.dp),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureToggle(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isChecked) MaterialTheme.colorScheme.primary else TextSecondary,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
