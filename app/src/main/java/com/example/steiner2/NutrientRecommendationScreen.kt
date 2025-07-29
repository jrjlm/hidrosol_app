package com.example.steiner2.ui

// NutrientRecommendationScreen.kt


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steiner2.model.NutrientRepository
import com.example.steiner2.model.RecommendedRange

@Composable
fun NutrientRecommendationScreen() {
    var selectedCrop by remember { mutableStateOf("Lechuga") }
    var selectedStage by remember { mutableStateOf("Vegetativo") }

    val availableCrops = NutrientRepository.recommendedRanges.map { it.crop }.distinct()
    val availableStages = NutrientRepository.recommendedRanges
        .filter { it.crop == selectedCrop }
        .map { it.stage }
        .distinct()

    val ranges: List<RecommendedRange> = NutrientRepository.getRanges(selectedCrop, selectedStage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Seleccionar Cultivo", style = MaterialTheme.typography.titleMedium)
        DropdownMenuField(
            value = selectedCrop,
            options = availableCrops,
            onOptionSelected = { selectedCrop = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Seleccionar Etapa", style = MaterialTheme.typography.titleMedium)
        DropdownMenuField(
            value = selectedStage,
            options = availableStages,
            onOptionSelected = { selectedStage = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Rangos recomendados:", style = MaterialTheme.typography.titleLarge)

        ranges.forEach { range ->
            Text("${range.nutrientId}: ${range.min} - ${range.max} mg/L")
        }
    }
}

@Composable
fun DropdownMenuField(
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(value)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
