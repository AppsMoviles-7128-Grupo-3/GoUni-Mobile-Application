package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.usecase.auth.EmailExistsUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdatePasswordByEmailUseCase
import com.example.gouni_mobile_application.presentation.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val emailExistsUseCase: EmailExistsUseCase,
    private val updatePasswordByEmailUseCase: UpdatePasswordByEmailUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<User>?>(null)
    val authState: StateFlow<UiState<User>?> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _updateState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val updateState: StateFlow<UiState<Unit>> = _updateState.asStateFlow()

    // Password reset states
    private val _forgotPasswordState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val forgotPasswordState: StateFlow<UiState<Unit>> = _forgotPasswordState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val resetPasswordState: StateFlow<UiState<Unit>> = _resetPasswordState.asStateFlow()

    fun loadCurrentUser(userId: String) {
        viewModelScope.launch {
            getUserByIdUseCase(userId).collect { user ->
                _currentUser.value = user
            }
        }
    }

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
                    .onSuccess {
                        _updateState.value = UiState.Success(Unit)
                        loadCurrentUser(user.id)
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

    // Simplified forgot password implementation - checks if email exists in database
    fun forgotPassword(email: String) {
        if (email.isBlank()) {
            _forgotPasswordState.value = UiState.Error("Por favor ingresa tu correo electrónico")
            return
        }

        if (!isValidEmail(email)) {
            _forgotPasswordState.value = UiState.Error("Por favor ingresa un correo electrónico válido")
            return
        }

        viewModelScope.launch {
            _forgotPasswordState.value = UiState.Loading
            try {
                // Check if email exists in the database
                val emailExists = emailExistsUseCase(email)
                
                if (emailExists) {
                    _forgotPasswordState.value = UiState.Success(Unit)
                } else {
                    _forgotPasswordState.value = UiState.Error("No se encontró una cuenta con este correo electrónico")
                }
            } catch (e: Exception) {
                _forgotPasswordState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    // Real reset password implementation - updates password in database
    fun resetPassword(newPassword: String, email: String) {
        if (newPassword.length < 6) {
            _resetPasswordState.value = UiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (email.isBlank()) {
            _resetPasswordState.value = UiState.Error("Error: Email no encontrado")
            return
        }

        viewModelScope.launch {
            _resetPasswordState.value = UiState.Loading
            try {
                // Update password in the database
                updatePasswordByEmailUseCase(email, newPassword)
                    .onSuccess {
                        _resetPasswordState.value = UiState.Success(Unit)
                    }
                    .onFailure { error ->
                        _resetPasswordState.value = UiState.Error(error.message ?: "Error al actualizar la contraseña")
                    }
            } catch (e: Exception) {
                _resetPasswordState.value = UiState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = null
    }

    fun resetUpdateState() {
        _updateState.value = UiState.Idle
    }

    fun resetForgotPasswordState() {
        _forgotPasswordState.value = UiState.Idle
    }

    fun resetResetPasswordState() {
        _resetPasswordState.value = UiState.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}