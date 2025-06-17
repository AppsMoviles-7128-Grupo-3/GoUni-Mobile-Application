package com.example.gouni_mobile_application.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Create Route") },
            label = { Text("Create") },
            selected = currentRoute == "create_route",
            onClick = {
                navController.navigate("create_route") {
                    popUpTo("create_route") { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "My Routes") },
            label = { Text("Routes") },
            selected = currentRoute == "my_routes",
            onClick = {
                navController.navigate("my_routes") {
                    popUpTo("my_routes") { inclusive = true }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = "Reservations") },
            label = { Text("Reservations") },
            selected = currentRoute == "reservations",
            onClick = {
                navController.navigate("reservations") {
                    popUpTo("reservations") { inclusive = true }
                }
            }
        )
    }
}