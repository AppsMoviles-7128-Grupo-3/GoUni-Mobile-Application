package com.example.gouni_mobile_application.presentation.views.car

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
fun CarEditView(
    car: Car,
    onCarUpdated: (Car) -> Unit,
    onNavigateBack: () -> Unit,
    viewModelFactory: ViewModelFactory
) {
    var color by remember { mutableStateOf(car.color) }
    var insuranceInfo by remember { mutableStateOf(car.insuranceInfo) }
    var insuranceBrand by remember { mutableStateOf(car.insuranceBrand) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val updateCarState by carViewModel.insertCarState.collectAsState()
    val deleteCarState by carViewModel.deleteCarState.collectAsState()

    LaunchedEffect(updateCarState) {
        if (updateCarState is UiState.Success) {

            val updatedCar = car.copy(
                color = color,
                insuranceInfo = insuranceInfo,
                insuranceBrand = insuranceBrand
            )
            onCarUpdated(updatedCar)
            carViewModel.resetInsertCarState()
        }
    }

    LaunchedEffect(deleteCarState) {
        if (deleteCarState is UiState.Success) {

            onNavigateBack()
            carViewModel.resetDeleteCarState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Editar Vehículo",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(
                onClick = { showDeleteDialog = true }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Car",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.padding(bottom = 60.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Información del Vehículo",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Marca: ${car.make}")
                Text("Modelo: ${car.model}")
                Text("Placa: ${car.licensePlate}")
                Text("Año: ${car.year}")
                Text("Registro: ${car.registrationNumber}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Color") },
            placeholder = { Text("Ej: Blanco") },
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

        Spacer(modifier = Modifier.height(32.dp))


        when (val currentState = updateCarState) {
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

        when (val currentState = deleteCarState) {
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
                    if (validateEditForm(color, insuranceInfo, insuranceBrand)) {
                        showSaveConfirmation = true
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = updateCarState !is UiState.Loading && deleteCarState !is UiState.Loading
            ) {
                if (updateCarState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar")
                }
            }
        }
    }


    if (showSaveConfirmation) {
        AlertDialog(
            onDismissRequest = { showSaveConfirmation = false },
            title = { Text("Confirmar Cambios") },
            text = {
                Text("¿Estás seguro de que quieres guardar los cambios en la información del vehículo?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveConfirmation = false
                        val updatedCar = car.copy(
                            color = color,
                            insuranceInfo = insuranceInfo,
                            insuranceBrand = insuranceBrand
                        )
                        carViewModel.insertCar(updatedCar) // Using insertCar for update (replace)
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSaveConfirmation = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Vehículo") },
            text = {
                Text("¿Estás seguro de que quieres eliminar el vehículo ${car.make} ${car.model} (${car.licensePlate})? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        carViewModel.deleteCar(car)
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun validateEditForm(
    color: String,
    insuranceInfo: String,
    insuranceBrand: String
): Boolean {
    return color.isNotBlank() &&
            insuranceInfo.isNotBlank() &&
            insuranceBrand.isNotBlank()
} 