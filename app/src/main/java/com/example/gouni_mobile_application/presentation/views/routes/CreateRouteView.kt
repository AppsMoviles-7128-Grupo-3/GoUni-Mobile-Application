package com.example.gouni_mobile_application.presentation.views.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.RoutesViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import java.time.DayOfWeek
import java.time.LocalTime

@Composable
fun CreateRouteScreen(
    userId: String,
    viewModel: RoutesViewModel,
    viewModelFactory: ViewModelFactory,
    onNavigateToCarRegistration: () -> Unit
) {
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var departureTime by remember { mutableStateOf("") }
    var arrivalTime by remember { mutableStateOf("") }
    var availableSeats by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val createRouteState by viewModel.createRouteState.collectAsState()

    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val carState by carViewModel.carState.collectAsState()

    LaunchedEffect(userId) {
        carViewModel.getCar(userId)
    }

    LaunchedEffect(createRouteState) {
        if (createRouteState is UiState.Success) {
            start = ""
            end = ""
            selectedDays = setOf()
            departureTime = ""
            arrivalTime = ""
            availableSeats = ""
            price = ""
            // delay
            kotlinx.coroutines.delay(2000)
            viewModel.resetCreateRouteState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Crear Ruta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val currentCarState = carState) {
            is UiState.Success -> {
                currentCarState.data?.let { car ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "Vehículo Registrado",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${car.make} ${car.model} - ${car.licensePlate}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = start,
                        onValueChange = { start = it },
                        label = { Text("Desde") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = end,
                        onValueChange = { end = it },
                        label = { Text("Hasta") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Seleccionar Días:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    val daysOfWeek = listOf(
                        DayOfWeek.MONDAY to "Lunes",
                        DayOfWeek.TUESDAY to "Martes",
                        DayOfWeek.WEDNESDAY to "Miércoles",
                        DayOfWeek.THURSDAY to "Jueves",
                        DayOfWeek.FRIDAY to "Viernes",
                        DayOfWeek.SATURDAY to "Sábado",
                        DayOfWeek.SUNDAY to "Domingo"
                    )

                    daysOfWeek.forEach { (day, displayName) ->
                        val isSelected = selectedDays.contains(day)

                        Card(
                            onClick = {
                                selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(40.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = departureTime,
                            onValueChange = { departureTime = it },
                            label = { Text("Salida") },
                            placeholder = { Text("08:00") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = arrivalTime,
                            onValueChange = { arrivalTime = it },
                            label = { Text("Llegada") },
                            placeholder = { Text("09:00") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = availableSeats,
                            onValueChange = { availableSeats = it },
                            label = { Text("Asientos Disponibles") },
                            modifier = Modifier.weight(2f)
                        )

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Precio") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (validateForm(start, end, selectedDays, departureTime, arrivalTime, availableSeats, price)) {
                                try {
                                    viewModel.createRoute(
                                        driverId = userId,
                                        carId = car.id,
                                        start = start,
                                        end = end,
                                        days = selectedDays.toList(),
                                        departureTime = LocalTime.parse(departureTime),
                                        arrivalTime = LocalTime.parse(arrivalTime),
                                        availableSeats = availableSeats.toInt(),
                                        price = price.toDouble()
                                    )
                                } catch (e: Exception) { }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = createRouteState !is UiState.Loading
                    ) {
                        if (createRouteState is UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Crear Ruta")
                        }
                    }

                    when (val currentState = createRouteState) {
                        is UiState.Success -> {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text(
                                    text = "¡Ruta creada exitosamente!",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        is UiState.Error -> {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = currentState.message,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        else -> {}
                    }
                } ?: run {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Vehículo Requerido",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Debe registrar un vehículo antes de crear rutas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    // Navigate to car registration
                                    onNavigateToCarRegistration()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Registrar Vehículo")
                            }
                        }
                    }
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error al verificar vehículo",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentCarState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

private fun validateForm(
    start: String,
    end: String,
    selectedDays: Set<DayOfWeek>,
    departureTime: String,
    arrivalTime: String,
    availableSeats: String,
    price: String
): Boolean {
    return start.isNotBlank() &&
            end.isNotBlank() &&
            selectedDays.isNotEmpty() &&
            departureTime.isNotBlank() &&
            arrivalTime.isNotBlank() &&
            availableSeats.isNotBlank() &&
            price.isNotBlank()
}