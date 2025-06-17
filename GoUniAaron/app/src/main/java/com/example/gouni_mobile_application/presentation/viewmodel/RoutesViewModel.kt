package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

class RoutesViewModel(
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase
) : ViewModel() {

    private val _routesState = MutableStateFlow<UiState<List<Route>>>(UiState.Success(emptyList()))
    val routesState: StateFlow<UiState<List<Route>>> = _routesState

    // Cambiar estado inicial a null para evitar loading permanente
    private val _createRouteState = MutableStateFlow<UiState<String>?>(null)
    val createRouteState: StateFlow<UiState<String>?> = _createRouteState

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
                        // Recargar las rutas después de crear una nueva
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

    fun resetCreateRouteState() {
        _createRouteState.value = null
    }
}