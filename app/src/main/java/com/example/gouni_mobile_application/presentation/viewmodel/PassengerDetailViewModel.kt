package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.usecase.route.GetRouteByIdUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PassengerDetailViewModel(
    private val getRouteByIdUseCase: GetRouteByIdUseCase
) : ViewModel() {

    private val _routeState = MutableStateFlow<UiState<Route>>(UiState.Idle)
    val routeState: StateFlow<UiState<Route>> = _routeState

    fun loadRoute(routeId: String) {
        viewModelScope.launch {
            _routeState.value = UiState.Loading
            try {
                getRouteByIdUseCase(routeId).collect { route ->
                    _routeState.value = UiState.Success(route) as UiState<Route>
                }
            } catch (e: Exception) {
                _routeState.value = UiState.Error(e.message ?: "Failed to load route")
            }
        }
    }
} 