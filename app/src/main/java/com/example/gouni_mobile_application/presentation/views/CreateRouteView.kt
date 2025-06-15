package com.example.gouni_mobile_application.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gouni_mobile_application.presentation.viewmodels.CreateRouteViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.DayOfWeek

@Preview(showBackground = true)
@Composable
fun CreateRouteView(viewModel: CreateRouteViewModel = viewModel()) {
    val start = viewModel.start
    val end = viewModel.end
    val selectedDays = viewModel.selectedDays
    val departureTime = viewModel.departureTime
    val arrivalTime = viewModel.arrivalTime

    val dayLabels = listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do")
    val dayValues = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    val PrimaryColor = Color(0xFF3F51B5)
    val AccentColor = Color(0xFF00BCD4)
    val ConfirmColor = Color(0xFF4CAF50)
    val TextFieldColor = Color(0xFFF5F5F5)
    val SelectedDayColor = PrimaryColor
    val UnselectedDayColor = Color(0xFFE0E0E0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Crear Nueva Ruta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = start,
            onValueChange = { viewModel.start = it },
            label = { Text("Desde") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = end,
            onValueChange = { viewModel.end = it },
            label = { Text("Hasta") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Días", fontWeight = FontWeight.Medium, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dayLabels.forEachIndexed { index, label ->
                val day = dayValues[index]
                val selected = selectedDays.contains(day)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (selected) SelectedDayColor else UnselectedDayColor)
                        .clickable { viewModel.toggleDaySelection(day) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = departureTime,
            onValueChange = { viewModel.departureTime = it },
            label = { Text("Hora de salida") },
            placeholder = { Text("HH:MM") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = arrivalTime,
            onValueChange = { viewModel.arrivalTime = it },
            label = { Text("Hora de llegada") },
            placeholder = { Text("HH:MM") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PrimaryColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.confirmRoute() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = start.isNotBlank() && end.isNotBlank()
                    && selectedDays.isNotEmpty()
                    && departureTime.isNotBlank() && arrivalTime.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            )
        ) {
            Text("Confirmar ruta", fontWeight = FontWeight.SemiBold)
        }

        if (viewModel.showConfirmation) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Ruta guardada correctamente",
                color = ConfirmColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
