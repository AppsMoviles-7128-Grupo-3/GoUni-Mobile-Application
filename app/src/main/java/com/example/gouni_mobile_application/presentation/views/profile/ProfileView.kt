package com.example.gouni_mobile_application.presentation.views.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.AuthViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory

@Composable
fun ProfileView(
    user: User,
    viewModel: AuthViewModel,
    userId: String,
    viewModelFactory: ViewModelFactory,
    onNavigateToCarRegistration: () -> Unit,
    onNavigateToUserEdit: () -> Unit,
    onNavigateToCarEdit: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var university by remember { mutableStateOf(user.university) }
    var userCode by remember { mutableStateOf(user.userCode) }
    var password by remember { mutableStateOf("") }
    var showEditOptions by remember { mutableStateOf(false) }

    val updateState by viewModel.updateState.collectAsState()
    
    // Car ViewModel
    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val carState by carViewModel.carState.collectAsState()
    val hasCarState by carViewModel.hasCarState.collectAsState()

    LaunchedEffect(userId) {
        carViewModel.hasCar(userId)
        carViewModel.getCar(userId)
    }

    LaunchedEffect(updateState) {
        if (updateState is UiState.Success) {
            isEditing = false
            viewModel.resetUpdateState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium
            )

            Box {
                IconButton(
                    onClick = { showEditOptions = !showEditOptions }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Options")
                }

                DropdownMenu(
                    expanded = showEditOptions,
                    onDismissRequest = { showEditOptions = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar Información Personal") },
                        onClick = {
                            showEditOptions = false
                            onNavigateToUserEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Editar Información del Vehículo") },
                        onClick = {
                            showEditOptions = false
                            onNavigateToCarEdit()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileInfoCard(
            title = "Información Personal",
            items = listOf(
                "Nombre" to user.name,
                "Email" to user.email,
                "Universidad" to user.university,
                "Código de Usuario" to user.userCode
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val currentCarState = carState) {
            is UiState.Success -> {
                currentCarState.data?.let { car ->
                    ProfileInfoCard(
                        title = "Información del Vehículo",
                        items = listOf(
                            "Marca" to car.make,
                            "Modelo" to car.model,
                            "Placa" to car.licensePlate,
                            "Color" to car.color,
                            "Año" to car.year.toString(),
                            "Seguro" to car.insuranceInfo,
                            "Marca de Seguro" to car.insuranceBrand,
                            "Registro" to car.registrationNumber
                        )
                    )
                } ?: run {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Información del Vehículo",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay vehículo registrado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onNavigateToCarRegistration,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registrar Vehículo")
                            }
                        }
                    }
                }
            }
            is UiState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is UiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error al cargar información del vehículo",
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

        when (val currentState = updateState) {
            is UiState.Success -> {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Perfil actualizado exitosamente",
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

@Composable
fun ProfileInfoCard(
    title: String,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$label:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (items.last() != (label to value)) {
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}