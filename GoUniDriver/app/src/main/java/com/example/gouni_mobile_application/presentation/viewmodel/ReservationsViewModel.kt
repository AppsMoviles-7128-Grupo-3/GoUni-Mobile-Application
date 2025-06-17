package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val getReservationsUseCase: GetReservationsUseCase
) : ViewModel() {

    private val _reservationsState = MutableStateFlow<UiState<List<StudentReservation>>>(UiState.Loading)
    val reservationsState: StateFlow<UiState<List<StudentReservation>>> = _reservationsState

    fun loadReservations(driverId: String) {
        viewModelScope.launch {
            try {
                getReservationsUseCase(driverId).collect { reservations ->
                    _reservationsState.value = UiState.Success(reservations)
                }
            } catch (e: Exception) {
                _reservationsState.value = UiState.Error(e.message ?: "Failed to load reservations")
            }
        }
    }
}