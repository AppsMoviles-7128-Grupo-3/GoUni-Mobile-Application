package com.example.gouni_mobile_application.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek



class CreateRouteViewModel() : ViewModel() {

    var start by mutableStateOf("")
    var end by mutableStateOf("")
    var selectedDays = mutableStateListOf<DayOfWeek>()
    var departureTime by mutableStateOf("")
    var arrivalTime by mutableStateOf("")
    var showConfirmation by mutableStateOf(false)

    fun toggleDaySelection(day: DayOfWeek) {
        if (selectedDays.contains(day)) {
            selectedDays.remove(day)
        } else {
            selectedDays.add(day)
        }
    }

    fun confirmRoute() {
        if (start.isBlank() || end.isBlank() || selectedDays.isEmpty()
            || departureTime.isBlank() || arrivalTime.isBlank()
        ) {
            showConfirmation = false
            return
        }

        viewModelScope.launch {
            delay(500)
            showConfirmation = true
        }
    }
}