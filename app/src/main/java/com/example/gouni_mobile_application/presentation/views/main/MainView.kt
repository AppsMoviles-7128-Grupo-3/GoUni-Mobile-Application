package com.example.gouni_mobile_application.presentation.views.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.presentation.navigation.BottomNavigation
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.reservations.ReservationsScreen
import com.example.gouni_mobile_application.presentation.views.routes.CreateRouteScreen
import com.example.gouni_mobile_application.presentation.views.routes.MyRoutesScreen

@Composable
fun MainScreen(
    navController: NavHostController,
    userId: String,
    application: GoUniApplication
) {
    val routesViewModelFactory = ViewModelFactory(
        loginUseCase = com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase(application.authRepository),
        registerUseCase = com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase(application.authRepository),
        getMyRoutesUseCase = GetMyRoutesUseCase(application.routeRepository),
        createRouteUseCase = CreateRouteUseCase(application.routeRepository),
        getReservationsUseCase = GetReservationsUseCase(application.reservationRepository)
    )

    Scaffold(
        bottomBar = { BottomNavigation(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "my_routes",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("create_route") {
                CreateRouteScreen(
                    userId = userId,
                    viewModel = viewModel(factory = routesViewModelFactory)
                )
            }
            composable("my_routes") {
                MyRoutesScreen(
                    userId = userId,
                    viewModel = viewModel(factory = routesViewModelFactory)
                )
            }
            composable("reservations") {
                ReservationsScreen(
                    userId = userId,
                    viewModel = viewModel(factory = routesViewModelFactory)
                )
            }
        }
    }
}