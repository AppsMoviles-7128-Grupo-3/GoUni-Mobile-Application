package com.example.gouni_mobile_application.presentation.views.reservations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.gouni_mobile_application.presentation.viewmodel.PassengerDetailViewModel
import com.example.gouni_mobile_application.presentation.viewmodel.ViewModelFactory
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerDetailView(
    reservation: StudentReservation,
    viewModelFactory: ViewModelFactory,
    onNavigateBack: () -> Unit
) {
    val passengerDetailViewModel: PassengerDetailViewModel = viewModel(factory = viewModelFactory)
    val routeState by passengerDetailViewModel.routeState.collectAsState()

    LaunchedEffect(reservation.routeId) {
        passengerDetailViewModel.loadRoute(reservation.routeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Detalle del Pasajero",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = reservation.studentName.first().toString().uppercase(),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = reservation.studentName,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = when (reservation.status) {
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.PENDING -> MaterialTheme.colorScheme.primary
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.ACCEPTED -> MaterialTheme.colorScheme.tertiary
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.REJECTED -> MaterialTheme.colorScheme.error
                                }
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = when (reservation.status) {
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.PENDING -> "PENDIENTE"
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.ACCEPTED -> "ACEPTADO"
                                    com.example.gouni_mobile_application.domain.model.ReservationStatus.REJECTED -> "RECHAZADO"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ModernInfoCard(
                    title = "Información del Pasajero",
                    icon = Icons.Default.Person,
                    items = listOf(
                        InfoItem("Nombre", reservation.studentName, Icons.Default.Person),
                        InfoItem("Edad", "${reservation.age} años", Icons.Default.Cake),
                        InfoItem("Código de Estudiante", reservation.universityId, Icons.Default.Badge),
                        InfoItem("Universidad", reservation.universityName.ifEmpty { "No especificada" }, Icons.Default.School),
                        InfoItem("Punto de Encuentro", reservation.meetingPlace, Icons.Default.LocationOn)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (val currentRouteState = routeState) {
                    is com.example.gouni_mobile_application.presentation.state.UiState.Success -> {
                        currentRouteState.data?.let { route ->
                            ModernInfoCard(
                                title = "Información de la Ruta",
                                icon = Icons.Default.Route,
                                items = listOf(
                                    InfoItem("Origen", route.start, Icons.Default.LocationOn),
                                    InfoItem("Destino", route.end, Icons.Default.LocationOn),
                                    InfoItem("Horario", "${route.departureTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${route.arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"))}", Icons.Default.Schedule),
                                    InfoItem("Días", route.days.joinToString(", ") {
                                        when(it) {
                                            java.time.DayOfWeek.MONDAY -> "Lun"
                                            java.time.DayOfWeek.TUESDAY -> "Mar"
                                            java.time.DayOfWeek.WEDNESDAY -> "Mié"
                                            java.time.DayOfWeek.THURSDAY -> "Jue"
                                            java.time.DayOfWeek.FRIDAY -> "Vie"
                                            java.time.DayOfWeek.SATURDAY -> "Sáb"
                                            java.time.DayOfWeek.SUNDAY -> "Dom"
                                        }
                                    }, Icons.Default.CalendarToday),
                                    InfoItem("Precio", "S/.${route.price.toInt()}", Icons.Default.AttachMoney),
                                    InfoItem("Asientos Disponibles", "${route.availableSeats}", Icons.Default.AirlineSeatReclineNormal)
                                )
                            )
                        } ?: run {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "No se pudo cargar la información de la ruta",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                    is com.example.gouni_mobile_application.presentation.state.UiState.Loading -> {
                        LoadingCard()
                    }
                    is com.example.gouni_mobile_application.presentation.state.UiState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = currentRouteState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                    else -> {
                        LoadingCard()
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ModernInfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    items: List<InfoItem>
) {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
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
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                if (items.last() != item) {
                    Divider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

data class InfoItem(
    val label: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) 