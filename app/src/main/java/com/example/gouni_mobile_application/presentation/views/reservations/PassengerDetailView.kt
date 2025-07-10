package com.example.gouni_mobile_application.presentation.views.reservations

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.presentation.viewmodel.PassengerDetailViewModel
import com.example.gouni_mobile_application.presentation.state.UiState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Badge
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerDetailView(
    reservation: StudentReservation,
    onNavigateBack: () -> Unit,
    viewModel: PassengerDetailViewModel,
    onNavigateToMyRoutes: () -> Unit
) {
    val routeState = viewModel.routeState.collectAsState()
    val userState = viewModel.userState.collectAsState()

    LaunchedEffect(reservation.routeId) {
        viewModel.loadRoute(reservation.routeId)
    }
    LaunchedEffect(reservation.passengerId) {
        viewModel.loadUser(reservation.passengerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de la Reserva",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onNavigateToMyRoutes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Mis Rutas", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reservation Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Passenger Initial (if available)
                    when (userState.value) {
                        is UiState.Success<*> -> {
                            val user = (userState.value as UiState.Success<com.example.gouni_mobile_application.domain.model.User?>).data
                            if (user != null && user.name.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.name.first().toString().uppercase(),
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                        else -> {}
                    }
                    Text(
                        text = "Reserva #${reservation.id}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    when (routeState.value) {
                        is UiState.Loading, UiState.Idle -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is UiState.Error -> Text("Error al cargar la ruta", color = MaterialTheme.colorScheme.error)
                        is UiState.Success<*> -> {
                            val route = (routeState.value as UiState.Success<com.example.gouni_mobile_application.domain.model.Route?>).data
                            if (route != null) {
                                Text("Ruta: ${route.start} → ${route.end}", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Horario: ${route.departureTime} - ${route.arrivalTime}", style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Precio: S/. ${route.price}", style = MaterialTheme.typography.bodyMedium)
                            } else {
                                Text("Ruta no encontrada", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Passenger Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Información del Pasajero", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    when (userState.value) {
                        is UiState.Loading, UiState.Idle -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is UiState.Error -> Text("Error al cargar pasajero", color = MaterialTheme.colorScheme.error)
                        is UiState.Success<*> -> {
                            val user = (userState.value as UiState.Success<com.example.gouni_mobile_application.domain.model.User?>).data
                            if (user != null) {
                                Text(user.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(user.email, style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(user.university, style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(user.userCode, style = MaterialTheme.typography.bodyMedium)
                                }
                            } else {
                                Text("Pasajero no encontrado", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
} 