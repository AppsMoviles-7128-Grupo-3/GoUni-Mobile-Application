package com.example.gouni_mobile_application.presentation.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.auth.SignInScreen
import com.example.gouni_mobile_application.presentation.views.auth.SignUpScreen
import com.example.gouni_mobile_application.presentation.views.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentUserId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val application = context.applicationContext as GoUniApplication

    val authViewModelFactory = ViewModelFactory(
        loginUseCase = LoginUseCase(application.authRepository),
        registerUseCase = RegisterUseCase(application.authRepository),
        getMyRoutesUseCase = com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase(application.routeRepository),
        createRouteUseCase = com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase(application.routeRepository),
        getReservationsUseCase = com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase(application.reservationRepository)
    )

    NavHost(
        navController = navController,
        startDestination = "signin"
    ) {
        composable("signin") {
            SignInScreen(
                viewModel = viewModel(factory = authViewModelFactory),
                onSignInSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate("main") {
                        popUpTo("signin") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = viewModel(factory = authViewModelFactory),
                onSignUpSuccess = { userId ->
                    currentUserId = userId
                    navController.navigate("main") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.popBackStack()
                }
            )
        }

        composable("main") {
            val mainNavController = rememberNavController()
            MainScreen(
                navController = mainNavController,
                userId = currentUserId ?: "",
                application = application
            )
        }
    }
}