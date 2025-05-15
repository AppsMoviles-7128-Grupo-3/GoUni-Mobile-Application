package com.example.gouni_mobile_application.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview
@Composable
fun PlanView() {
    val plans = listOf(
        Plan(
            title = "Basic",
            price = "Free",
            benefits = listOf("2 Viajes por dia", "Publicidad incluida", "Acceso limitado a funciones", "Soporte de basico de la comunidad")
        ),
        Plan(
            title = "Student Plan",
            price = "S/. 10 por mes",
            benefits = listOf("Invita un amigo gratis", "Viajes ilimitados", "Doble de puntos en el primer mes",
                "Reservaciones anticipadas", "Descuentos exclusivos", "Atencion al cliente personalizada")
        ),
        Plan(
            title = "Premium Plan",
            price = "S/. 15 por mes",
            benefits = listOf("Invita un amigo gratis", "Viajes ilimitados", "Puntos acumulables",
                "Precios exclusivos", "Notificaciones en tiempo real", "Acceso a eventos exclusivos")
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nuestro Planes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                modifier = Modifier.size(28.dp)
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(35.dp, 130.dp) //Aqui estoy jugando para q quede centrado, si quieres lo borras
        ) {
            items(plans) { plan ->
                PlanCard(plan)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {

            }) {
                Text("Omitir")
            }
        }

    }
}

@Composable
fun PlanCard(plan: Plan) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(280.dp)
            .height(350.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = plan.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = plan.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    plan.benefits.forEach { benefit ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = benefit)
                        }
                    }
                }
            }

            Column {
                Button(
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Suscribirse")
                }

                Text(
                    text = "Cancela en cualquier momento",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

data class Plan(
    val title: String,
    val price: String,
    val benefits: List<String>
)