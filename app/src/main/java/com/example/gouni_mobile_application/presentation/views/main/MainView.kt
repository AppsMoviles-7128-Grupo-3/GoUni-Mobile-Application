package com.example.gouni_mobile_application.presentation.views.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.car.DeleteCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.GetCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.HasCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.InsertCarUseCase
import com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase
import com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase
import com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase
import com.example.gouni_mobile_application.presentation.navigation.BottomNavigation
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.car.CarEditView
import com.example.gouni_mobile_application.presentation.views.car.CarRegistrationView
import com.example.gouni_mobile_application.presentation.views.profile.ProfileView
import com.example.gouni_mobile_application.presentation.views.profile.UserEditView
import com.example.gouni_mobile_application.presentation.views.reservations.ReservationsScreen
import com.example.gouni_mobile_application.presentation.views.routes.CreateRouteScreen
import com.example.gouni_mobile_application.presentation.views.routes.MyRoutesView
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.state.UiState

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
        getReservationsUseCase = GetReservationsUseCase(application.reservationRepository),
        getCarUseCase = GetCarUseCase(application.carRepository),
        insertCarUseCase = InsertCarUseCase(application.carRepository),
        hasCarUseCase = HasCarUseCase(application.carRepository),
        deleteCarUseCase = DeleteCarUseCase(application.carRepository),
        getUserByIdUseCase = GetUserByIdUseCase(application.authRepository)
    )

    val authViewModel: com.example.gouni_mobile_application.presentation.viewmodel.AuthViewModel = viewModel(factory = viewModelFactory)
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            authViewModel.loadCurrentUser(userId)
        }
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
                    viewModel = viewModel(factory = viewModelFactory),
                    viewModelFactory = viewModelFactory,
                    onNavigateToCarRegistration = {
                        navController.navigate("car_registration")
                    }
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
                        viewModel = authViewModel,
                        userId = userId,
                        viewModelFactory = viewModelFactory,
                        onNavigateToCarRegistration = {
                            navController.navigate("car_registration")
                        },
                        onNavigateToUserEdit = {
                            navController.navigate("user_edit")
                        },
                        onNavigateToCarEdit = {
                            navController.navigate("car_edit")
                        }
                    )
                }
            }
            composable("car_registration") {
                CarRegistrationView(
                    userId = userId,
                    onCarRegistered = { car ->
                        navController.popBackStack()
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    viewModelFactory = viewModelFactory
                )
            }
            composable("user_edit") {
                currentUser?.let { user ->
                    UserEditView(
                        user = user,
                        onUserUpdated = {
                            navController.popBackStack()
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        viewModelFactory = viewModelFactory
                    )
                }
            }
            composable("car_edit") {
                val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
                val carState by carViewModel.carState.collectAsState()
                
                LaunchedEffect(userId) {
                    carViewModel.getCar(userId)
                }
                
                when (val currentCarState = carState) {
                    is UiState.Success -> {
                        currentCarState.data?.let { car ->
                            CarEditView(
                                car = car,
                                onCarUpdated = { updatedCar ->
                                    navController.popBackStack()
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModelFactory = viewModelFactory
                            )
                        } ?: run {
                            navController.popBackStack()
                        }
                    }
                    is UiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Error -> {
                        navController.popBackStack()
                    }
                    else -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}