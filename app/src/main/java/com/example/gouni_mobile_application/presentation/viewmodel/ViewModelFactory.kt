package com.example.gouni_mobile_application.presentation.viewmodel

import android.app.Application
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
import com.example.gouni_mobile_application.domain.usecase.car.GetCarByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.car.HasCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.InsertCarUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByPassengerUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsByDriverUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetRouteByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetRoutePolylineUseCase

class ViewModelFactory(
    private val application: Application,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val getReservationsByRouteUseCase: GetReservationsByRouteUseCase,
    private val getReservationsByPassengerUseCase: GetReservationsByPassengerUseCase,
    private val getReservationsByDriverUseCase: GetReservationsByDriverUseCase,
    private val getCarUseCase: GetCarUseCase,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val insertCarUseCase: InsertCarUseCase,
    private val hasCarUseCase: HasCarUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val emailExistsUseCase: EmailExistsUseCase,
    private val updatePasswordByEmailUseCase: UpdatePasswordByEmailUseCase,
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getRoutePolylineUseCase: GetRoutePolylineUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(loginUseCase, registerUseCase, updateUserUseCase, logoutUseCase, getUserByIdUseCase, emailExistsUseCase, updatePasswordByEmailUseCase) as T
            }
            modelClass.isAssignableFrom(RoutesViewModel::class.java) -> {
                RoutesViewModel(getMyRoutesUseCase, createRouteUseCase, deleteRouteUseCase, getRoutePolylineUseCase, application) as T
            }
            modelClass.isAssignableFrom(ReservationsViewModel::class.java) -> {
                ReservationsViewModel(getReservationsByRouteUseCase, getReservationsByPassengerUseCase, getReservationsByDriverUseCase) as T
            }
            modelClass.isAssignableFrom(CarViewModel::class.java) -> {
                CarViewModel(getCarUseCase, getCarByIdUseCase, insertCarUseCase, hasCarUseCase, deleteCarUseCase) as T
            }
            modelClass.isAssignableFrom(PassengerDetailViewModel::class.java) -> {
                PassengerDetailViewModel(getRouteByIdUseCase, getUserByIdUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}