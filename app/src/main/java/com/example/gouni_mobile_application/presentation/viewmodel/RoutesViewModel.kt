package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

class RoutesViewModel(
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase
) : ViewModel() {

    private val _routesState = MutableStateFlow<UiState<List<Route>>>(UiState.Success(emptyList()))
    val routesState: StateFlow<UiState<List<Route>>> = _routesState

    private val _createRouteState = MutableStateFlow<UiState<String>?>(null)
    val createRouteState: StateFlow<UiState<String>?> = _createRouteState

    private val _deleteRouteState = MutableStateFlow<UiState<Unit>?>(null)
    val deleteRouteState: StateFlow<UiState<Unit>?> = _deleteRouteState

    fun loadRoutes(driverId: String) {
        viewModelScope.launch {
            try {
                getMyRoutesUseCase(driverId).collect { routes ->
                    _routesState.value = UiState.Success(routes)
                }
            } catch (e: Exception) {
                _routesState.value = UiState.Error(e.message ?: "Failed to load routes")
            }
        }
    }

    fun createRoute(
        driverId: String,
        carId: String,
        start: String,
        end: String,
        days: List<DayOfWeek>,
        departureTime: LocalTime,
        arrivalTime: LocalTime,
        availableSeats: Int,
        price: Double
    ) {
        viewModelScope.launch {
            _createRouteState.value = UiState.Loading
            try {
                val route = Route(
                    driverId = driverId,
                    carId = carId,
                    start = start,
                    end = end,
                    days = days,
                    departureTime = departureTime,
                    arrivalTime = arrivalTime,
                    availableSeats = availableSeats,
                    price = price
                )
                createRouteUseCase(route)
                    .onSuccess { routeId ->
                        _createRouteState.value = UiState.Success(routeId)
                        loadRoutes(driverId)
                    }
                    .onFailure { error ->
                        _createRouteState.value = UiState.Error(error.message ?: "Failed to create route")
                    }
            } catch (e: Exception) {
                _createRouteState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun deleteRoute(routeId: String, driverId: String) {
        viewModelScope.launch {
            _deleteRouteState.value = UiState.Loading
            try {
                deleteRouteUseCase(routeId)
                    .onSuccess {
                        _deleteRouteState.value = UiState.Success(Unit)
                        loadRoutes(driverId)
                    }
                    .onFailure { error ->
                        _deleteRouteState.value = UiState.Error(error.message ?: "Failed to delete route")
                    }
            } catch (e: Exception) {
                _deleteRouteState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetCreateRouteState() {
        _createRouteState.value = null
    }

    fun resetDeleteRouteState() {
        _deleteRouteState.value = null
    }
}