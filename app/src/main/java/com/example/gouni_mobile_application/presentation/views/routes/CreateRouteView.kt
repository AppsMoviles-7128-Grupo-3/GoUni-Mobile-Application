package com.example.gouni_mobile_application.presentation.views.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.RoutesViewModel
import java.time.DayOfWeek
import java.time.LocalTime

@Composable
fun CreateRouteScreen(
    userId: String,
    viewModel: RoutesViewModel
) {
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var departureTime by remember { mutableStateOf("") }
    var arrivalTime by remember { mutableStateOf("") }
    var availableSeats by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val createRouteState by viewModel.createRouteState.collectAsState()

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

        OutlinedTextField(
            value = start,
            onValueChange = { start = it },
            label = { Text("Desde") },
            placeholder = { Text("Ej: Universidad Central") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = end,
            onValueChange = { end = it },
            label = { Text("Hasta") },
            placeholder = { Text("Ej: Centro Comercial") },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = selectedDays.contains(day),
                        onValueChange = { isSelected ->
                            selectedDays = if (isSelected) {
                                selectedDays + day
                            } else {
                                selectedDays - day
                            }
                        }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedDays.contains(day),
                    onCheckedChange = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(displayName)
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
                label = { Text("Asientos") },
                placeholder = { Text("4") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                placeholder = { Text("5000") },
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
                            start = start,
                            end = end,
                            days = selectedDays.toList(),
                            departureTime = LocalTime.parse(departureTime),
                            arrivalTime = LocalTime.parse(arrivalTime),
                            availableSeats = availableSeats.toInt(),
                            price = price.toDouble()
                        )
                    } catch (e: Exception) {
                        // Manejar error de parsing
                    }
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

        // Mostrar mensajes de estado
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