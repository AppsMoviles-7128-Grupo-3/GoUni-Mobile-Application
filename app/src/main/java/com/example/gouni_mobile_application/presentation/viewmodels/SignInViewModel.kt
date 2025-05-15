package com.example.gouni_mobile_application.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gouni_mobile_application.data.models.UserCredentials
import com.example.gouni_mobile_application.data.remote.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.gouni_mobile_application.data.repository.AuthRepositoryImpl

class SignInViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl(AuthService())

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun signIn() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = repository.login(
                UserCredentials(email.value, password.value)
            )

            _isLoading.value = false
            result.onSuccess {
                _isLoggedIn.value = true
            }.onFailure {
                _errorMessage.value = it.message
            }
        }
    }
}