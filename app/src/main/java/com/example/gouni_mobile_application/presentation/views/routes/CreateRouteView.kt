package com.example.gouni_mobile_application.presentation.views.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.RoutesViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.gouni_mobile_application.BuildConfig
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.window.Popup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.net.URLEncoder
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import com.example.gouni_mobile_application.R
import androidx.compose.material.icons.filled.Close

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRouteScreen(
    userId: String,
    viewModel: RoutesViewModel,
    viewModelFactory: ViewModelFactory,
    onNavigateToCarRegistration: () -> Unit,
    onNavigateBack: () -> Unit,
    navController: androidx.navigation.NavHostController
) {
    var startDisplay by remember { mutableStateOf("") }
    var endDisplay by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var startLat by remember { mutableStateOf<Double?>(null) }
    var startLng by remember { mutableStateOf<Double?>(null) }
    var endLat by remember { mutableStateOf<Double?>(null) }
    var endLng by remember { mutableStateOf<Double?>(null) }
    var selectedDays by remember { mutableStateOf(setOf<DayOfWeek>()) }
    var departureTime by remember { mutableStateOf(LocalTime.of(8, 0)) }
    var arrivalTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var availableSeats by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    
    var showDepartureTimePicker by remember { mutableStateOf(false) }
    var showArrivalTimePicker by remember { mutableStateOf(false) }
    var showSuccessPopup by remember { mutableStateOf(false) }

    var startPlaceId by remember { mutableStateOf<String?>(null) }
    var endPlaceId by remember { mutableStateOf<String?>(null) }

    val createRouteState by viewModel.createRouteState.collectAsState()

    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val carState by carViewModel.carState.collectAsState()

    LaunchedEffect(userId) {
        carViewModel.getCar(userId)
    }

    LaunchedEffect(createRouteState) {
        if (createRouteState is UiState.Success) {
            showSuccessPopup = true
            viewModel.resetCreateRouteState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Crear Ruta",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding() // This handles keyboard padding
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                when (val currentCarState = carState) {
                    is UiState.Success -> {
                        currentCarState.data?.let { car ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Vehículo Registrado",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "${car.make} ${car.model} - ${car.licensePlate}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Detalles de la Ruta",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            PlacesAutocompleteTextField(
                                label = "Desde",
                                value = startDisplay,
                                onValueChange = { startDisplay = it },
                                onPlaceSelected = { description, lat, lng, placeId ->
                                    startDisplay = description
                                    start = description.substringBefore(",").trim()
                                    startLat = lat
                                    startLng = lng
                                    startPlaceId = placeId
                                },
                                onClear = { startLat = null; startLng = null; startPlaceId = null; start = ""; startDisplay = "" }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PlacesAutocompleteTextField(
                                label = "Hasta",
                                value = endDisplay,
                                onValueChange = { endDisplay = it },
                                onPlaceSelected = { description, lat, lng, placeId ->
                                    endDisplay = description
                                    end = description.substringBefore(",").trim()
                                    endLat = lat
                                    endLng = lng
                                    endPlaceId = placeId
                                },
                                onClear = { endLat = null; endLng = null; endPlaceId = null; end = ""; endDisplay = "" }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Días de la Semana",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            val daysOfWeek = listOf(
                                DayOfWeek.MONDAY to "Lu",
                                DayOfWeek.TUESDAY to "Ma",
                                DayOfWeek.WEDNESDAY to "Mi",
                                DayOfWeek.THURSDAY to "Ju",
                                DayOfWeek.FRIDAY to "Vi",
                                DayOfWeek.SATURDAY to "Sá",
                                DayOfWeek.SUNDAY to "Do"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                daysOfWeek.forEach { (day, displayName) ->
                                    val isSelected = selectedDays.contains(day)

                                    Card(
                                        onClick = {
                                            selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                                        },
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = if (isSelected) 2.dp else 1.dp
                                        ),
                                        shape = MaterialTheme.shapes.extraLarge,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .height(48.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = displayName,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) 
                                                        FontWeight.SemiBold
                                                    else 
                                                        FontWeight.Normal
                                                ),
                                                color = if (isSelected) 
                                                    MaterialTheme.colorScheme.onPrimary 
                                                else 
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Horarios",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showDepartureTimePicker = true },
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp, 
                                        MaterialTheme.colorScheme.outline
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = departureTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                OutlinedButton(
                                    onClick = { showArrivalTimePicker = true },
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp, 
                                        MaterialTheme.colorScheme.outline
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Información Adicional",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = availableSeats,
                                    onValueChange = { availableSeats = it },
                                    label = { Text("Asientos") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )

                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Precio (S/.)") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedLabelColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    if (startLat != null && startLng != null && endLat != null && endLng != null &&
                                        availableSeats.isNotBlank() && price.isNotBlank()) {
                                        viewModel.createRoute(
                                            userId = userId,
                                            carId = car.id,
                                            start = start,
                                            end = end,
                                            days = selectedDays.toList(),
                                            departureTime = departureTime,
                                            arrivalTime = arrivalTime,
                                            availableSeats = availableSeats.toIntOrNull() ?: 0,
                                            price = price.toDoubleOrNull() ?: 0.0
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = startLat != null && startLng != null && endLat != null && endLng != null &&
                                        availableSeats.isNotBlank() && price.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(16.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = "Crear Ruta",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        } ?: run {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Vehículo Requerido",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = MaterialTheme.colorScheme.error,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Debes registrar un vehículo antes de crear rutas",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = onNavigateToCarRegistration,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        ),
                                        shape = MaterialTheme.shapes.medium
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
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    is UiState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error al cargar vehículo",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                                    ),
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentCarState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDepartureTimePicker) {
        val departureTimeState = rememberTimePickerState(
            initialHour = departureTime.hour,
            initialMinute = departureTime.minute
        )
        
        AlertDialog(
            onDismissRequest = { showDepartureTimePicker = false },
            title = { Text("Hora de Salida") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        departureTime = LocalTime.of(departureTimeState.hour, departureTimeState.minute)
                        showDepartureTimePicker = false 
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDepartureTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(
                    state = departureTimeState
                )
            }
        )
    }

    if (showArrivalTimePicker) {
        val arrivalTimeState = rememberTimePickerState(
            initialHour = arrivalTime.hour,
            initialMinute = arrivalTime.minute
        )
        
        AlertDialog(
            onDismissRequest = { showArrivalTimePicker = false },
            title = { Text("Hora de Llegada") },
            confirmButton = {
                TextButton(
                    onClick = { 
                        arrivalTime = LocalTime.of(arrivalTimeState.hour, arrivalTimeState.minute)
                        showArrivalTimePicker = false 
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showArrivalTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(
                    state = arrivalTimeState
                )
            }
        )
    }

    if (showSuccessPopup) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Ruta Creada Exitosamente!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Text(
                    text = "Tu ruta ha sido creada y está disponible para los pasajeros.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessPopup = false
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Mis Rutas")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        )
    }
}

private fun validateForm(
    start: String,
    end: String,
    selectedDays: Set<DayOfWeek>,
    departureTime: LocalTime,
    arrivalTime: LocalTime,
    availableSeats: String,
    price: String
): Boolean {
    return start.isNotBlank() &&
            end.isNotBlank() &&
            selectedDays.isNotEmpty() &&
            availableSeats.isNotBlank() &&
            price.isNotBlank()
}

@Composable
fun PlacesAutocompleteTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onPlaceSelected: (String, Double, Double, String) -> Unit,
    onClear: (() -> Unit)? = null
) {
    var suggestions by remember { mutableStateOf(listOf<Pair<String, String>>()) } // Pair<description, placeId>
    var isLoading by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
        OutlinedTextField(
            value = value,
            onValueChange = {
                println("User typed: $it")
                onValueChange(it)
                if (it.length > 2) {
                    coroutineScope.launch {
                        isLoading = true
                        errorMessage = null
                        val apiKey = context.getString(R.string.google_maps_api_key)
                        val result = getPlaceSuggestionsWithError(it, apiKey)
                        println("API returned: $result")
                        suggestions = result.first
                        errorMessage = result.second
                        isLoading = false
                        expanded = suggestions.isNotEmpty()
                    }
                } else {
                    suggestions = emptyList()
                    expanded = false
                    errorMessage = null
                }
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            singleLine = true,
            isError = errorMessage != null,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = {
                        onValueChange("")
                        onClear?.invoke()
                        suggestions = emptyList()
                        expanded = false
                        errorMessage = null
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Borrar campo"
                        )
                    }
                }
            }
        )
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            suggestions.forEach { (description, placeId) ->
                DropdownMenuItem(
                    text = { Text(description) },
                    onClick = {
                        coroutineScope.launch {
                            val apiKey = context.getString(R.string.google_maps_api_key)
                            val coords = getPlaceCoordinates(placeId, apiKey)
                            if (coords != null) {
                                onPlaceSelected(description, coords.first, coords.second, placeId)
                                suggestions = emptyList()
                                expanded = false
                                errorMessage = null
                            } else {
                                errorMessage = "No se pudo obtener la ubicación."
                            }
                        }
                    }
                )
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

suspend fun getPlaceSuggestionsWithError(query: String, apiKey: String): Pair<List<Pair<String, String>>, String?> {
    return withContext(Dispatchers.IO) {
        val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                URLEncoder.encode(query, "UTF-8") +
                "&key=$apiKey&language=es"
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        val suggestions = mutableListOf<Pair<String, String>>()
        var error: String? = null
        try {
            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                println("Places API response: $response")
                val json = JSONObject(response)
                val status = json.optString("status")
                if (status != "OK") {
                    error = json.optString("error_message", status)
                }
                val predictions = json.optJSONArray("predictions")
                if (predictions != null) {
                    for (i in 0 until predictions.length()) {
                        val item = predictions.getJSONObject(i)
                        val description = item.getString("description")
                        val placeId = item.getString("place_id")
                        suggestions.add(description to placeId)
                    }
                }
            } else {
                error = "HTTP ${conn.responseCode}"
            }
        } catch (e: Exception) {
            error = "Error: ${e.localizedMessage}"
            println("Exception in getPlaceSuggestions: $e")
        } finally {
            conn.disconnect()
        }
        suggestions to error
    }
}