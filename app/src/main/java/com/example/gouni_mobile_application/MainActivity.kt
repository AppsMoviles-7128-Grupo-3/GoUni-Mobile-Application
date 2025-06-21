package com.example.gouni_mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gouni_mobile_application.presentation.navigation.AppNavigation
import com.example.gouni_mobile_application.ui.theme.GoUniApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoUniApplication {
                AppNavigation()
            }
        }
    }
}