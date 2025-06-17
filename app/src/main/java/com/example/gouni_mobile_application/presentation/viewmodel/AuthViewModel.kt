package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    // Cambiar el estado inicial a un estado neutro
    private val _authState = MutableStateFlow<UiState<User>?>(null)
    val authState: StateFlow<UiState<User>?> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = UiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                loginUseCase(email, password)
                    .onSuccess { user ->
                        _authState.value = UiState.Success(user)
                        _currentUser.value = user
                    }
                    .onFailure { error ->
                        _authState.value = UiState.Error(error.message ?: "Error en el inicio de sesión")
                    }
            } catch (e: Exception) {
                _authState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String, university: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || university.isBlank()) {
            _authState.value = UiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                registerUseCase(name, email, password, university)
                    .onSuccess { user ->
                        _authState.value = UiState.Success(user)
                        _currentUser.value = user
                    }
                    .onFailure { error ->
                        _authState.value = UiState.Error(error.message ?: "Error en el registro")
                    }
            } catch (e: Exception) {
                _authState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = null
    }
}