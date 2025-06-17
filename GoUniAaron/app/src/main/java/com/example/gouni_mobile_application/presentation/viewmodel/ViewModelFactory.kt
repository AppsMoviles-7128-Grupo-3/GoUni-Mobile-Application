package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase

class ViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val getReservationsUseCase: GetReservationsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(loginUseCase, registerUseCase) as T
            }
            modelClass.isAssignableFrom(RoutesViewModel::class.java) -> {
                RoutesViewModel(getMyRoutesUseCase, createRouteUseCase) as T
            }
            modelClass.isAssignableFrom(ReservationsViewModel::class.java) -> {
                ReservationsViewModel(getReservationsUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}