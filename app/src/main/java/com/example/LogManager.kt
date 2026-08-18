package com.example

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogManager {
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun log(message: String) {
        val time = dateFormat.format(Date())
        val formattedMessage = "[$time] $message"
        val currentList = _logs.value.toMutableList()
        currentList.add(0, formattedMessage) // Add to top
        if (currentList.size > 100) {
            currentList.removeLast()
        }
        _logs.value = currentList
    }

    fun clear() {
        _logs.value = emptyList()
    }
}
