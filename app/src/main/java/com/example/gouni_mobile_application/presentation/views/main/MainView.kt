package com.example.gouni_mobile_application.presentation.views.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.EmailExistsUseCase
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
import com.example.gouni_mobile_application.presentation.navigation.BottomNavigation
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.car.CarEditView
import com.example.gouni_mobile_application.presentation.views.car.CarRegistrationView
import com.example.gouni_mobile_application.presentation.views.profile.ProfileView
import com.example.gouni_mobile_application.presentation.views.profile.UserEditView
import com.example.gouni_mobile_application.presentation.views.reservations.PassengerDetailView
import com.example.gouni_mobile_application.presentation.views.reservations.ReservationsScreen
import com.example.gouni_mobile_application.presentation.views.routes.CreateRouteScreen
import com.example.gouni_mobile_application.presentation.views.routes.MyRoutesView
import com.example.gouni_mobile_application.presentation.views.routes.RouteDetailView
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.PassengerDetailViewModel
import com.example.gouni_mobile_application.data.di.DataModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    navController: NavHostController,
    userId: String,
    application: GoUniApplication,
    onLogout: () -> Unit
) {
    val getReservationsByRouteUseCase = GetReservationsByRouteUseCase(application.reservationRepository)
    val getReservationsByPassengerUseCase = GetReservationsByPassengerUseCase(application.reservationRepository)
    val getReservationsByDriverUseCase = GetReservationsByDriverUseCase(application.reservationRepository)
    val getRoutePolylineUseCase = GetRoutePolylineUseCase(DataModule.getMapRepository())

    val viewModelFactory = ViewModelFactory(
        application = application,
        loginUseCase = com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase(application.authRepository),
        registerUseCase = com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase(application.authRepository),
        updateUserUseCase = UpdateUserUseCase(application.authRepository),
        logoutUseCase = LogoutUseCase(application.authRepository),
        getMyRoutesUseCase = GetMyRoutesUseCase(application.routeRepository),
        createRouteUseCase = CreateRouteUseCase(application.routeRepository),
        deleteRouteUseCase = DeleteRouteUseCase(application.routeRepository),
        getReservationsByRouteUseCase = getReservationsByRouteUseCase,
        getReservationsByPassengerUseCase = getReservationsByPassengerUseCase,
        getReservationsByDriverUseCase = getReservationsByDriverUseCase,
        getCarUseCase = GetCarUseCase(application.carRepository),
        getCarByIdUseCase = application.getCarByIdUseCase,
        insertCarUseCase = InsertCarUseCase(application.carRepository),
        hasCarUseCase = HasCarUseCase(application.carRepository),
        deleteCarUseCase = DeleteCarUseCase(application.carRepository),
        getUserByIdUseCase = GetUserByIdUseCase(application.authRepository),
        emailExistsUseCase = EmailExistsUseCase(application.authRepository),
        updatePasswordByEmailUseCase = UpdatePasswordByEmailUseCase(application.authRepository),
        getRouteByIdUseCase = GetRouteByIdUseCase(application.routeRepository),
        getRoutePolylineUseCase = getRoutePolylineUseCase
    )

    val authViewModel: com.example.gouni_mobile_application.presentation.viewmodel.AuthViewModel = viewModel(factory = viewModelFactory)
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var selectedRoute by remember { mutableStateOf<com.example.gouni_mobile_application.domain.model.Route?>(null) }
    var selectedReservation by remember { mutableStateOf<com.example.gouni_mobile_application.domain.model.StudentReservation?>(null) }

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            authViewModel.loadCurrentUser(userId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "GoUni Driver",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            authViewModel.logout()
                            onLogout()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = { 
            BottomNavigation(navController) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "my_routes",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("my_routes") {
                MyRoutesView(
                    userId = userId,
                    viewModel = viewModel(factory = viewModelFactory),
                    onCreateRouteClick = {
                        navController.navigate("create_route")
                    },
                    onRouteClick = { route ->
                        selectedRoute = route
                        navController.navigate("route_detail")
                    }
                )
            }
            composable("create_route") {
                CreateRouteScreen(
                    userId = userId,
                    viewModel = viewModel(factory = viewModelFactory),
                    viewModelFactory = viewModelFactory,
                    onNavigateToCarRegistration = {
                        navController.navigate("car_registration")
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    navController = navController
                )
            }
            composable("reservations") {
                val reservationsViewModel: com.example.gouni_mobile_application.presentation.viewmodel.ReservationsViewModel = viewModel(factory = viewModelFactory)
                LaunchedEffect(userId) {
                    reservationsViewModel.loadReservationsByDriver(userId)
                }
                ReservationsScreen(
                    viewModel = reservationsViewModel,
                    onReservationClick = { reservation ->
                        selectedReservation = reservation
                        navController.navigate("passenger_detail")
                    }
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
                            authViewModel.loadCurrentUser(userId)
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
                val carState: UiState<com.example.gouni_mobile_application.domain.model.Car?> by carViewModel.carState.collectAsState()
                LaunchedEffect(userId) {
                    carViewModel.getCar(userId)
                }
                when (carState) {
                    is UiState.Success -> {
                        val car = (carState as UiState.Success<com.example.gouni_mobile_application.domain.model.Car?>).data
                        if (car != null) {
                            CarEditView(
                                car = car,
                                onCarUpdated = {
                                    carViewModel.getCar(userId)
                                    navController.popBackStack()
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModelFactory = viewModelFactory
                            )
                        } else {
                            Text("No car found for this user.")
                        }
                    }
                    is UiState.Loading, UiState.Idle -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Error -> {
                        val message = (carState as UiState.Error).message
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error loading car: $message")
                        }
                    }
                }
            }
            composable("route_detail?startLat={startLat}&startLng={startLng}&endLat={endLat}&endLng={endLng}",
                arguments = listOf(
                    navArgument("startLat") { nullable = true; type = NavType.StringType },
                    navArgument("startLng") { nullable = true; type = NavType.StringType },
                    navArgument("endLat") { nullable = true; type = NavType.StringType },
                    navArgument("endLng") { nullable = true; type = NavType.StringType }
                )
            ) { backStackEntry ->
                val startLat = backStackEntry.arguments?.getString("startLat")?.toDoubleOrNull()
                val startLng = backStackEntry.arguments?.getString("startLng")?.toDoubleOrNull()
                val endLat = backStackEntry.arguments?.getString("endLat")?.toDoubleOrNull()
                val endLng = backStackEntry.arguments?.getString("endLng")?.toDoubleOrNull()
                selectedRoute?.let { route ->
                    RouteDetailView(
                        route = route,
                        viewModelFactory = viewModelFactory,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToReservations = { navController.navigate("reservations") },
                        startLat = startLat,
                        startLng = startLng,
                        endLat = endLat,
                        endLng = endLng
                    )
                }
            }
            composable("passenger_detail") {
                selectedReservation?.let { reservation ->
                    val passengerDetailViewModel: PassengerDetailViewModel = viewModel(factory = viewModelFactory)
                    PassengerDetailView(
                        reservation = reservation,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        viewModel = passengerDetailViewModel,
                        onNavigateToMyRoutes = {
                            navController.navigate("my_routes")
                        }
                    )
                }
            }
        }
    }
}