package com.example.gouni_mobile_application.presentation.views.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.RoutesViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MyRoutesView(
    userId: String,
    viewModel: RoutesViewModel
) {
    val routesState by viewModel.routesState.collectAsState()
    val deleteState by viewModel.deleteRouteState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var routeToDelete by remember { mutableStateOf<Route?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadRoutes(userId)
    }

    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success) {
            viewModel.resetDeleteRouteState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Rutas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val currentState = routesState) {
            is UiState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cargando rutas...")
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
            is UiState.Success -> {
                if (currentState.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No tienes rutas creadas",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ve a 'Crear' para agregar tu primera ruta",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentState.data) { route ->
                            RouteCard(
                                route = route,
                                onDeleteClick = {
                                    routeToDelete = route
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showDeleteDialog && routeToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Ruta") },
            text = {
                Text("¿Estás seguro de que quieres eliminar la ruta de ${routeToDelete!!.start} a ${routeToDelete!!.end}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRoute(routeToDelete!!.id, userId)
                        showDeleteDialog = false
                        routeToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        routeToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun RouteCard(
    route: Route,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${route.start} → ${route.end}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Route",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Días: ${route.days.joinToString(", ") {
                    when(it) {
                        java.time.DayOfWeek.MONDAY -> "Lun"
                        java.time.DayOfWeek.TUESDAY -> "Mar"
                        java.time.DayOfWeek.WEDNESDAY -> "Mié"
                        java.time.DayOfWeek.THURSDAY -> "Jue"
                        java.time.DayOfWeek.FRIDAY -> "Vie"
                        java.time.DayOfWeek.SATURDAY -> "Sáb"
                        java.time.DayOfWeek.SUNDAY -> "Dom"
                    }
                }}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Horario: ${route.departureTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${route.arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Asientos: ${route.availableSeats}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Precio: S/.${route.price.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}