package com.example.gouni_mobile_application.presentation.views.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.AuthViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEditView(
    user: User,
    onUserUpdated: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModelFactory: ViewModelFactory
) {
    var email by remember { mutableStateOf(user.email) }
    var university by remember { mutableStateOf(user.university) }
    var userCode by remember { mutableStateOf(user.userCode) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)
    val updateState by authViewModel.updateState.collectAsState()

    LaunchedEffect(updateState) {
        if (updateState is UiState.Success) {
            onUserUpdated()
            authViewModel.resetUpdateState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Text(
            text = "Editar Información Personal",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.padding(bottom = 100.dp))

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
                    text = "Información Personal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Nombre: ${user.name}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("nuevo@email.com") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = university,
            onValueChange = { university = it },
            label = { Text("Universidad") },
            placeholder = { Text("Tu universidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userCode,
            onValueChange = { userCode = it },
            label = { Text("Código de Estudiante") },
            placeholder = { Text("Tu código") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Nueva Contraseña") },
            placeholder = { Text("Dejar vacío para mantener actual") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Nueva Contraseña") },
            placeholder = { Text("Repetir nueva contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (val currentState = updateState) {
            is UiState.Success -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Información actualizada exitosamente",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            is UiState.Error -> {
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
                    if (validateEditForm(email, password, confirmPassword, university, userCode)) {
                        showSaveConfirmation = true
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = updateState !is UiState.Loading
            ) {
                if (updateState is UiState.Loading) {
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
                Text("¿Estás seguro de que quieres guardar los cambios en tu información personal?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSaveConfirmation = false
                        val updatedUser = user.copy(
                            email = email,
                            university = university,
                            userCode = userCode
                        )
                        val finalPassword = if (password.isBlank()) "123456" else password // Default password
                        authViewModel.updateUser(updatedUser, finalPassword)
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
}

private fun validateEditForm(
    email: String,
    password: String,
    confirmPassword: String,
    university: String,
    userCode: String
): Boolean {
    val isEmailValid = email.isNotBlank() && email.contains("@")
    val isPasswordValid = password.isBlank() || (password.length >= 6 && password == confirmPassword)
    
    return isEmailValid && isPasswordValid && university.isNotBlank() && userCode.isNotBlank()
} 