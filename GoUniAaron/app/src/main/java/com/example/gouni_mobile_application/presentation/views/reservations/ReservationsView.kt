package com.example.gouni_mobile_application.presentation.views.reservations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.presentation.state.UiState
import com.example.gouni_mobile_application.presentation.viewmodel.ReservationsViewModel

@Composable
fun ReservationsScreen(
    userId: String,
    viewModel: ReservationsViewModel
) {
    val reservationsState by viewModel.reservationsState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadReservations(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Reservations",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val currentState = reservationsState) {
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
                        Text("No reservations yet")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentState.data) { reservation ->
                            StudentReservationCard(reservation = reservation)
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
}

@Composable
fun StudentReservationCard(reservation: StudentReservation) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = reservation.studentName.first().toString(),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reservation.studentName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Age: ${reservation.age}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Meeting: ${reservation.meetingPlace}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "ID: ${reservation.universityId}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column {
                Text(
                    text = reservation.status.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = when (reservation.status) {
                        com.example.gouni_mobile_application.domain.model.ReservationStatus.PENDING -> MaterialTheme.colorScheme.primary
                        com.example.gouni_mobile_application.domain.model.ReservationStatus.ACCEPTED -> MaterialTheme.colorScheme.tertiary
                        com.example.gouni_mobile_application.domain.model.ReservationStatus.REJECTED -> MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}