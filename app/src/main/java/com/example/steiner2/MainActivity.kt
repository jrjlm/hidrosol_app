package com.example.steiner2
// ----- Importaciones necesarias -----
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api // Si usas controles de Material 3
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider // Para un ejemplo de control de entrada
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.steiner2.ui.theme.Steiner2Theme

// ----- Fin de Importaciones -----

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Steiner2Theme { // <<--- 3. USA TU TEMA AQUÍ
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FertilizerCalculatorScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Slider en Material 3
@Composable
fun FertilizerCalculatorScreen(modifier: Modifier = Modifier) {
    // Estados para los porcentajes de entrada
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

        // El gráfico Steiner
        // Asegúrate de que la importación de SteinerTriangleGraphic sea correcta
        // import com.example.teststeiner.SteinerTriangleGraphic // (debería estar al principio del archivo)
        SteinerTriangleGraphic(
            modifier = Modifier.size(300.dp), // Dale un tamaño fijo o usa pesos en un layout
            percentA = percentN,
            percentB = percentP,
            percentC = percentK
            // Puedes pasar `graphPadding` si quieres uno diferente al por defecto
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Controles de ejemplo para cambiar los valores
        Text("N: ${String.format("%.1f", percentN)}%")
        Slider(
            value = percentN,
            onValueChange = { percentN = it },
            valueRange = 0f..100f
        )

        Text("P: ${String.format("%.1f", percentP)}%")
        Slider(
            value = percentP,
            onValueChange = { percentP = it },
            valueRange = 0f..100f
        )

        Text("K: ${String.format("%.1f", percentK)}%")
        Slider(
            value = percentK,
            onValueChange = { percentK = it },
            valueRange = 0f..100f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recordatorio: la lógica de normalización está DENTRO de SteinerTriangleGraphic
        val sum = percentN + percentP + percentK
        if (Math.abs(sum - 100f) > 0.01f) {
            Text(
                "Suma actual: ${String.format("%.1f", sum)}% (se normalizará a 100% en el gráfico)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Steiner2Theme { // <<--- 4. USA TU TEMA AQUÍ TAMBIÉN
        FertilizerCalculatorScreen()
    }
}