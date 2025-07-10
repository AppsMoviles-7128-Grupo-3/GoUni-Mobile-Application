package com.example.gouni_mobile_application.presentation.views.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.ReservationsViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.CarViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import java.time.format.DateTimeFormatter
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.example.gouni_mobile_application.presentation.viewmodel.RoutesViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.MapUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext
import com.example.gouni_mobile_application.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDetailView(
    route: Route,
    viewModelFactory: ViewModelFactory,
    onNavigateBack: () -> Unit,
    onNavigateToReservations: (String) -> Unit,
    startLat: Double? = null,
    startLng: Double? = null,
    endLat: Double? = null,
    endLng: Double? = null,
    startPlaceId: String? = null,
    endPlaceId: String? = null
) {
    val reservationsViewModel: ReservationsViewModel = viewModel(factory = viewModelFactory)
    val carViewModel: CarViewModel = viewModel(factory = viewModelFactory)
    val routesViewModel: RoutesViewModel = viewModel(factory = viewModelFactory)
    val reservationsState by reservationsViewModel.reservationsState.collectAsState()
    val carState by carViewModel.carByIdState.collectAsState()
    val mapState by routesViewModel.mapState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(route.id, startLat, startLng, endLat, endLng, startPlaceId, endPlaceId) {
        reservationsViewModel.loadReservationsByRoute(route.id)
        carViewModel.getCarById(route.carId)
        if (startLat != null && startLng != null && endLat != null && endLng != null) {
            routesViewModel.setMapCoordinates(startLat, startLng, endLat, endLng)
        } else if (startPlaceId != null && endPlaceId != null) {
            coroutineScope.launch {
                val apiKey = context.getString(R.string.google_maps_api_key)
                val startCoords = getPlaceCoordinates(startPlaceId, apiKey)
                val endCoords = getPlaceCoordinates(endPlaceId, apiKey)
                if (startCoords != null && endCoords != null) {
                    val (startLatVal, startLngVal) = startCoords
                    val (endLatVal, endLngVal) = endCoords
                    routesViewModel.setMapCoordinates(startLatVal, startLngVal, endLatVal, endLngVal)
                } else {
                    routesViewModel.setMapError("No se pudieron obtener las coordenadas de los lugares.")
                }
            }
        } else {
            routesViewModel.setMapError("No hay información de ubicación disponible para esta ruta.")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Detalle de Ruta",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Desde",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = route.start,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Hasta",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.End
                                )
                                Text(
                                    text = route.end,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Horario",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${route.departureTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${route.arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                            
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Días",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = route.days.joinToString(", ") {
                                        when(it) {
                                            java.time.DayOfWeek.MONDAY -> "Lun"
                                            java.time.DayOfWeek.TUESDAY -> "Mar"
                                            java.time.DayOfWeek.WEDNESDAY -> "Mié"
                                            java.time.DayOfWeek.THURSDAY -> "Jue"
                                            java.time.DayOfWeek.FRIDAY -> "Vie"
                                            java.time.DayOfWeek.SATURDAY -> "Sáb"
                                            java.time.DayOfWeek.SUNDAY -> "Dom"
                                        }
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Vehículo",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        when (val currentCarState = carState) {
                            is UiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            is UiState.Success -> {
                                val car = currentCarState.data
                                if (car != null) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = "Marca y Modelo",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = "${car.make} ${car.model}",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }
                                            
                                            Column(
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "Color",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = car.color,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }
                                        }
                                        
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = "Placa",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = car.licensePlate,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    ),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            
                                            Column(
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    text = "Año",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = car.year.toString(),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                Icons.Default.DirectionsCar,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Información del vehículo no disponible",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                            is UiState.Error -> {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Error al cargar información del vehículo: ${currentCarState.message}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            else -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cargando información del vehículo...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Asientos Disponibles",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${route.availableSeats}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Precio",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "S/.${route.price.toInt()}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }

            // --- Default Map (no markers, no polyline) ---
            item {
                val defaultLatLng = com.google.android.gms.maps.model.LatLng(-12.0464, -77.0428) // Lima, Peru
                val cameraPositionState = rememberCameraPositionState {
                    position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(defaultLatLng, 12f)
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    cameraPositionState = cameraPositionState
                ) {}
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.People,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Pasajeros",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            
                            Button(
                                onClick = { onNavigateToReservations(route.id) },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ver Todos", style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        when (val currentState = reservationsState) {
                            is UiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            is UiState.Success -> {
                                val routeReservations = currentState.data.filter { it.routeId == route.id }
                                if (routeReservations.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                Icons.Default.People,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(48.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Sin pasajeros",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        routeReservations.take(3).forEach { reservation ->
                                            PassengerPreviewCard(reservation = reservation)
                                        }
                                        if (routeReservations.size > 3) {
                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                ),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(
                                                    text = "Y ${routeReservations.size - 3} pasajero(s) más...",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            is UiState.Error -> {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Error al cargar pasajeros: ${currentState.message}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            else -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cargando pasajeros...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun PassengerPreviewCard(reservation: StudentReservation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar del pasajero",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Pasajero: ${reservation.passengerId}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                )
                Text(
                    text = "Reserva: ${reservation.id}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

// Helper function to fetch coordinates from placeId
suspend fun getPlaceCoordinates(placeId: String, apiKey: String): Pair<Double, Double>? {
    return withContext(Dispatchers.IO) {
        val url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=$placeId&key=$apiKey&language=es"
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        try {
            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                val result = json.getJSONObject("result")
                val location = result.getJSONObject("geometry").getJSONObject("location")
                val lat = location.getDouble("lat")
                val lng = location.getDouble("lng")
                return@withContext lat to lng
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            conn.disconnect()
        }
        null
    }
}