package com.example.gouni_mobile_application.presentation.views.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.presentation.navigation.BottomNavigation
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.profile.ProfileView
import com.example.gouni_mobile_application.presentation.views.reservations.ReservationsScreen
import com.example.gouni_mobile_application.presentation.views.routes.CreateRouteScreen
import com.example.gouni_mobile_application.presentation.views.routes.MyRoutesView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    navController: NavHostController,
    userId: String,
    application: GoUniApplication,
    onLogout: () -> Unit
) {
    val viewModelFactory = ViewModelFactory(
        loginUseCase = com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase(application.authRepository),
        registerUseCase = com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase(application.authRepository),
        updateUserUseCase = UpdateUserUseCase(application.authRepository),
        logoutUseCase = LogoutUseCase(application.authRepository),
        getMyRoutesUseCase = GetMyRoutesUseCase(application.routeRepository),
        createRouteUseCase = CreateRouteUseCase(application.routeRepository),
        deleteRouteUseCase = DeleteRouteUseCase(application.routeRepository),
        getReservationsUseCase = GetReservationsUseCase(application.reservationRepository)
    )

    val authViewModel: com.example.gouni_mobile_application.presentation.viewmodel.AuthViewModel = viewModel(factory = viewModelFactory)
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(userId) {
        authViewModel.login("test@example.com", "123456")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GoUni Driver") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
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
                    viewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable("my_routes") {
                MyRoutesView(
                    userId = userId,
                    viewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable("reservations") {
                ReservationsScreen(
                    userId = userId,
                    viewModel = viewModel(factory = viewModelFactory)
                )
            }
            composable("profile") {
                currentUser?.let { user ->
                    ProfileView(
                        user = user,
                        viewModel = authViewModel
                    )
                }
            }
        }
    }
}