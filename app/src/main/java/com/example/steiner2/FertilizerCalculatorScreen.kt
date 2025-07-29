// FertilizerCalculatorScreen.kt
package com.example.steiner2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steiner2.SteinerTriangleGraphic  // Asegúrate de la ruta correcta de importación
import com.example.steiner2.ui.theme.Steiner2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FertilizerCalculatorScreen(modifier: Modifier = Modifier) {
    var percentN by remember { mutableFloatStateOf(40f) }
    var percentP by remember { mutableFloatStateOf(30f) }
    var percentK by remember { mutableFloatStateOf(30f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Triángulo de Steiner para Fertilizantes",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        SteinerTriangleGraphic(
            modifier = Modifier.size(300.dp),
            percentA = percentN,
            percentB = percentP,
            percentC = percentK
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Uso de Triple para simplificar la destructuración
        val sliders = listOf(
            Triple("N", percentN) { v: Float -> percentN = v },
            Triple("P", percentP) { v: Float -> percentP = v },
            Triple("K", percentK) { v: Float -> percentK = v }
        )

        sliders.forEach { (label, value, onChange) ->
            Text("$label: ${"%.1f".format(value)}%")
            Slider(
                value = value,
                onValueChange = onChange,
                valueRange = 0f..100f
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val sum = percentN + percentP + percentK
        if (kotlin.math.abs(sum - 100f) > 0.01f) {
            Text(
                "Suma actual: ${"%.1f".format(sum)}% (se normalizará en el gráfico)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FertilizerCalculatorPreview() {
    Steiner2Theme { FertilizerCalculatorScreen() }
}