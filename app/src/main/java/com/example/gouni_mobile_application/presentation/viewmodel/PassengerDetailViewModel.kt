package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.usecase.route.GetRouteByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PassengerDetailViewModel(
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _routeState = MutableStateFlow<UiState<Route>>(UiState.Idle)
    val routeState: StateFlow<UiState<Route>> = _routeState

    private val _userState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val userState: StateFlow<UiState<User>> = _userState

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

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _userState.value = UiState.Loading
            try {
                getUserByIdUseCase(userId).collect { user ->
                    _userState.value = UiState.Success(user) as UiState<User>
                }
            } catch (e: Exception) {
                _userState.value = UiState.Error(e.message ?: "Failed to load user")
            }
        }
    }
} 