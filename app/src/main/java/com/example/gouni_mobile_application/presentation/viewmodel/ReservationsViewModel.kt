package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByPassengerUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByDriverUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val getReservationsByRouteUseCase: GetReservationsByRouteUseCase,
    private val getReservationsByPassengerUseCase: GetReservationsByPassengerUseCase,
    private val getReservationsByDriverUseCase: GetReservationsByDriverUseCase
) : ViewModel() {

    private val _reservationsState = MutableStateFlow<UiState<List<StudentReservation>>>(UiState.Loading)
    val reservationsState: StateFlow<UiState<List<StudentReservation>>> = _reservationsState

    fun loadReservationsByRoute(routeId: String) {
        viewModelScope.launch {
            try {
                getReservationsByRouteUseCase(routeId).collect { reservations ->
                    _reservationsState.value = UiState.Success(reservations)
                }
            } catch (e: Exception) {
                _reservationsState.value = UiState.Error(e.message ?: "Failed to load reservations")
            }
        }
    }

    fun loadReservationsByPassenger(passengerId: String) {
        viewModelScope.launch {
            try {
                getReservationsByPassengerUseCase(passengerId).collect { reservations ->
                    _reservationsState.value = UiState.Success(reservations)
                }
            } catch (e: Exception) {
                _reservationsState.value = UiState.Error(e.message ?: "Failed to load reservations")
            }
        }
    }

    fun loadReservationsByDriver(driverId: String) {
        viewModelScope.launch {
            try {
                getReservationsByDriverUseCase(driverId).collect { reservations ->
                    _reservationsState.value = UiState.Success(reservations)
                }
            } catch (e: Exception) {
                _reservationsState.value = UiState.Error(e.message ?: "Failed to load reservations")
            }
        }
    }
}