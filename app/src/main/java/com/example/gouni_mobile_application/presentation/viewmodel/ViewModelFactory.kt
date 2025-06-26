package com.example.gouni_mobile_application.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gouni_mobile_application.domain.usecase.auth.EmailExistsUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdatePasswordByEmailUseCase
import com.example.gouni_mobile_application.domain.usecase.car.DeleteCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.GetCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.HasCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.InsertCarUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase

class ViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val getReservationsUseCase: GetReservationsUseCase,
    private val getCarUseCase: GetCarUseCase,
    private val insertCarUseCase: InsertCarUseCase,
    private val hasCarUseCase: HasCarUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val emailExistsUseCase: EmailExistsUseCase,
    private val updatePasswordByEmailUseCase: UpdatePasswordByEmailUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(loginUseCase, registerUseCase, updateUserUseCase, logoutUseCase, getUserByIdUseCase, emailExistsUseCase, updatePasswordByEmailUseCase) as T
            }
            modelClass.isAssignableFrom(RoutesViewModel::class.java) -> {
                RoutesViewModel(getMyRoutesUseCase, createRouteUseCase, deleteRouteUseCase) as T
            }
            modelClass.isAssignableFrom(ReservationsViewModel::class.java) -> {
                ReservationsViewModel(getReservationsUseCase) as T
            }
            modelClass.isAssignableFrom(CarViewModel::class.java) -> {
                CarViewModel(getCarUseCase, insertCarUseCase, hasCarUseCase, deleteCarUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}