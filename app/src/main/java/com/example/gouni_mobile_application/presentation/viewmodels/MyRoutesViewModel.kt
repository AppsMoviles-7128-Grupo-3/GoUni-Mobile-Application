package com.example.gouni_mobile_application.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.models.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime

class MyRoutesViewModel(
    private val routeRepository: RouteRepository? = null
) : ViewModel() {

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (routeRepository != null) {
                    _routes.value = routeRepository.getRoutes()
                } else {
                    // Sample data for preview/testing
                    loadSampleData()
                }
            } catch (e: Exception) {
                // Handle error - could add error state
                loadSampleData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadSampleData() {
        _routes.value = listOf(
            Route(
                start = "San Miguel - UPC",
                end = "Miraflores",
                days = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY
                ),
                departureTime = LocalTime.of(15, 30),
                arrivalTime = LocalTime.of(16, 15)
            ),
            Route(
                start = "USIL",
                end = "Magdalena",
                days = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
                ),
                departureTime = LocalTime.of(15, 30),
                arrivalTime = LocalTime.of(16, 0)
            ),
            Route(
                start = "San Isidro - UPC",
                end = "San Miguel",
                days = listOf(
                    DayOfWeek.TUESDAY,
                    DayOfWeek.THURSDAY
                ),
                departureTime = LocalTime.of(15, 30),
                arrivalTime = LocalTime.of(16, 30)
            )
        )
    }

    fun onCreateNewRoute() {
        // This would typically navigate to CreateRouteView
        // You can handle navigation here or in the view
    }
}