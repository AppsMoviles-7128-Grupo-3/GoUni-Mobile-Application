package com.example.gouni_mobile_application.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gouni_mobile_application.domain.models.Route
import com.example.gouni_mobile_application.presentation.viewmodels.MyRoutesViewModel
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MyRoutesView(
    navController: NavController? = null,
    viewModel: MyRoutesViewModel = viewModel()
) {
    val routes by viewModel.routes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRoutes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Mis Rutas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Routes List
        if (routes.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No tienes rutas creadas",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController?.navigate("createRoute")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crear nueva ruta")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(routes) { route ->
                    RouteCard(route = route)
                }

                // Add new route button at the bottom
                item {
                    Button(
                        onClick = {
                            navController?.navigate("createRoute")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Crear nueva ruta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteCard(route: Route) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Route icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFFBDBDBD),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Simple placeholder - you can replace with actual icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Route info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${route.start} - ${route.end}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatDays(route.days),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mañana ${route.departureTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Ver ruta button
            Button(
                onClick = { /* Navigate to route details */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF616161)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "Ver ruta",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun formatDays(days: List<DayOfWeek>): String {
    val dayMap = mapOf(
        DayOfWeek.MONDAY to "Lun",
        DayOfWeek.TUESDAY to "Mar",
        DayOfWeek.WEDNESDAY to "Mié",
        DayOfWeek.THURSDAY to "Jue",
        DayOfWeek.FRIDAY to "Vie",
        DayOfWeek.SATURDAY to "Sáb",
        DayOfWeek.SUNDAY to "Dom"
    )

    return days.sortedBy { it.value }
        .joinToString(", ") { dayMap[it] ?: it.name }
}

// Preview function for testing
@Preview(showBackground = true)
@Composable
fun MyRoutesViewPreview() {
    MyRoutesView()
}