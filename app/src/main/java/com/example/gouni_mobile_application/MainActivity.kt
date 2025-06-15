package com.example.gouni_mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gouni_mobile_application.presentation.views.CreateRouteView
import com.example.gouni_mobile_application.presentation.views.SignInView
import com.example.gouni_mobile_application.presentation.views.SignUpView
import com.example.gouni_mobile_application.ui.theme.GoUniMobileApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        val navController = rememberNavController()
        /*NavHost(navController = navController, startDestination = "signin") {
            composable("signin") {
                SignInView(navController = navController)
            }
            composable("signup") {
                SignUpView()
            }
        }*/

        NavHost(navController = navController, startDestination = "signin") {
            composable("signin") {
                SignInView(navController = navController)
            }
            composable("createRoute") {
                CreateRouteView()
            }
            composable("signup") {
                SignUpView()
            }
        }
    }
}