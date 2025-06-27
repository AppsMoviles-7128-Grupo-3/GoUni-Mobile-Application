package com.example.gouni_mobile_application.presentation.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.gouni_mobile_application.GoUniApplication
import com.example.gouni_mobile_application.domain.usecase.auth.LoginUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.LogoutUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.RegisterUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdateUserUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.GetUserByIdUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.EmailExistsUseCase
import com.example.gouni_mobile_application.domain.usecase.auth.UpdatePasswordByEmailUseCase
import com.example.gouni_mobile_application.domain.usecase.car.DeleteCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.GetCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.HasCarUseCase
import com.example.gouni_mobile_application.domain.usecase.car.InsertCarUseCase
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import com.example.gouni_mobile_application.presentation.views.auth.SignInScreen
import com.example.gouni_mobile_application.presentation.views.auth.SignUpView
import com.example.gouni_mobile_application.presentation.views.auth.ForgotPasswordView
import com.example.gouni_mobile_application.presentation.views.auth.ResetPasswordView
import com.example.gouni_mobile_application.presentation.views.main.MainView

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentUserId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val application = context.applicationContext as GoUniApplication

    val authViewModelFactory = ViewModelFactory(
        loginUseCase = LoginUseCase(application.authRepository),
        registerUseCase = RegisterUseCase(application.authRepository),
        updateUserUseCase = UpdateUserUseCase(application.authRepository),
        logoutUseCase = LogoutUseCase(application.authRepository),
        getMyRoutesUseCase = com.example.gouni_mobile_application.domain.usecase.route.GetMyRoutesUseCase(application.routeRepository),
        createRouteUseCase = com.example.gouni_mobile_application.domain.usecase.route.CreateRouteUseCase(application.routeRepository),
        deleteRouteUseCase = com.example.gouni_mobile_application.domain.usecase.route.DeleteRouteUseCase(application.routeRepository),
        getReservationsUseCase = com.example.gouni_mobile_application.domain.usecase.reservation.GetReservationsUseCase(application.reservationRepository),
        getCarUseCase = GetCarUseCase(application.carRepository),
        insertCarUseCase = InsertCarUseCase(application.carRepository),
        hasCarUseCase = HasCarUseCase(application.carRepository),
        deleteCarUseCase = DeleteCarUseCase(application.carRepository),
        getUserByIdUseCase = GetUserByIdUseCase(application.authRepository),
        emailExistsUseCase = EmailExistsUseCase(application.authRepository),
        updatePasswordByEmailUseCase = UpdatePasswordByEmailUseCase(application.authRepository),
        getRouteByIdUseCase = com.example.gouni_mobile_application.domain.usecase.route.GetRouteByIdUseCase(application.routeRepository)
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
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }

        composable("signup") {
            SignUpView(
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

        composable("forgot_password") {
            ForgotPasswordView(
                viewModel = viewModel(factory = authViewModelFactory),
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToResetPassword = { email ->
                    val encodedEmail = java.net.URLEncoder.encode(email, "UTF-8")
                    navController.navigate("reset_password/$encodedEmail")
                }
            )
        }

        composable("reset_password/{email}") { backStackEntry ->
            val encodedEmail = backStackEntry.arguments?.getString("email") ?: ""
            val email = try {
                java.net.URLDecoder.decode(encodedEmail, "UTF-8")
            } catch (e: Exception) {
                ""
            }
            ResetPasswordView(
                viewModel = viewModel(factory = authViewModelFactory),
                email = email,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSignIn = {
                    navController.navigate("signin") {
                        popUpTo("signin") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            val mainNavController = rememberNavController()
            MainView(
                navController = mainNavController,
                userId = currentUserId ?: "",
                application = application,
                onLogout = {
                    currentUserId = null
                    navController.navigate("signin") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}