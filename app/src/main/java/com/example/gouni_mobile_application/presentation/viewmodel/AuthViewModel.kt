package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<User>?>(null)
    val authState: StateFlow<UiState<User>?> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _updateState = MutableStateFlow<UiState<User>?>(null)
    val updateState: StateFlow<UiState<User>?> = _updateState

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

    fun register(name: String, email: String, password: String, university: String, userCode: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || university.isBlank() || userCode.isBlank()) {
            _authState.value = UiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _authState.value = UiState.Loading
            try {
                registerUseCase(name, email, password, university, userCode)
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

    fun updateUser(user: User, password: String) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            try {
                updateUserUseCase(user, password)
                    .onSuccess { updatedUser ->
                        _updateState.value = UiState.Success(updatedUser)
                        _currentUser.value = updatedUser
                    }
                    .onFailure { error ->
                        _updateState.value = UiState.Error(error.message ?: "Error al actualizar")
                    }
            } catch (e: Exception) {
                _updateState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _currentUser.value = null
            _authState.value = null
        }
    }

    fun resetAuthState() {
        _authState.value = null
    }

    fun resetUpdateState() {
        _updateState.value = null
    }
}