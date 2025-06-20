package com.example.gouni_mobile_application.presentation.views.car

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarRegistrationView(
    userId: String,
    onCarRegistered: (Car) -> Unit,
    onNavigateBack: () -> Unit,
    viewModelFactory: ViewModelFactory
) {
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var licensePlate by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var insuranceInfo by remember { mutableStateOf("") }
    var insuranceBrand by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var showRegistrationConfirmation by remember { mutableStateOf(false) }

    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val insertCarState by carViewModel.insertCarState.collectAsState()

    LaunchedEffect(insertCarState) {
        if (insertCarState is UiState.Success) {

            val car = Car(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId,
                make = make,
                model = model,
                licensePlate = licensePlate,
                color = color,
                year = year.toInt(),
                insuranceInfo = insuranceInfo,
                insuranceBrand = insuranceBrand,
                registrationNumber = registrationNumber
            )
            onCarRegistered(car)
            carViewModel.resetInsertCarState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registrar Vehículo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = make,
            onValueChange = { make = it },
            label = { Text("Marca") },
            placeholder = { Text("Ej: Toyota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Modelo") },
            placeholder = { Text("Ej: Corolla") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = licensePlate,
            onValueChange = { licensePlate = it },
            label = { Text("Placa") },
            placeholder = { Text("Ej: ABC123") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Color") },
            placeholder = { Text("Ej: Blanco") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Año") },
            placeholder = { Text("Ej: 2020") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = insuranceInfo,
            onValueChange = { insuranceInfo = it },
            label = { Text("Información de Seguro") },
            placeholder = { Text("Número de póliza") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = insuranceBrand,
            onValueChange = { insuranceBrand = it },
            label = { Text("Marca de Seguro") },
            placeholder = { Text("Ej: Allianz") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registrationNumber,
            onValueChange = { registrationNumber = it },
            label = { Text("Número de Registro") },
            placeholder = { Text("Número de registro vehicular") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))


        when (val currentState = insertCarState) {
            is UiState.Error -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            else -> {}
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    if (validateForm(make, model, licensePlate, color, year, insuranceInfo, insuranceBrand, registrationNumber)) {
                        showRegistrationConfirmation = true
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = insertCarState !is UiState.Loading
            ) {
                if (insertCarState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrar")
                }
            }
        }
    }


    if (showRegistrationConfirmation) {
        AlertDialog(
            onDismissRequest = { showRegistrationConfirmation = false },
            title = { Text("Confirmar Registro") },
            text = {
                Text("¿Estás seguro de que quieres registrar el vehículo ${make} ${model} (${licensePlate})?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegistrationConfirmation = false
                        val car = Car(
                            id = java.util.UUID.randomUUID().toString(),
                            userId = userId,
                            make = make,
                            model = model,
                            licensePlate = licensePlate,
                            color = color,
                            year = year.toInt(),
                            insuranceInfo = insuranceInfo,
                            insuranceBrand = insuranceBrand,
                            registrationNumber = registrationNumber
                        )
                        carViewModel.insertCar(car)
                    }
                ) {
                    Text("Registrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRegistrationConfirmation = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun validateForm(
    make: String,
    model: String,
    licensePlate: String,
    color: String,
    year: String,
    insuranceInfo: String,
    insuranceBrand: String,
    registrationNumber: String
): Boolean {
    return make.isNotBlank() &&
            model.isNotBlank() &&
            licensePlate.isNotBlank() &&
            color.isNotBlank() &&
            year.isNotBlank() &&
            year.toIntOrNull() != null &&
            year.toInt() > 1900 &&
            year.toInt() <= 2025 &&
            insuranceInfo.isNotBlank() &&
            insuranceBrand.isNotBlank() &&
            registrationNumber.isNotBlank()
} 