package com.example.steiner2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.steiner2.data.NutrientRepository
import com.example.steiner2.model.NutrientRecipe

/**
 * Editor dinámico de recetas: sliders para cada nutriente.
 */
@Composable
fun RecipeEditor(
    crop: String,
    stage: String,
    onRecipeChange: (NutrientRecipe) -> Unit
) {
    val nutrients = NutrientRepository.nutrients
    val ranges = NutrientRepository.getRanges(crop, stage)
        .associateBy { it.nutrientId }

    // Estado inicial para cada nutriente
    val stateMap = remember {
        nutrients.associate { n ->
            n.id to mutableFloatStateOf(ranges[n.id]?.min ?: 0f)
        }.toMutableMap()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Receta para $crop — $stage")

        nutrients.forEach { nutrient ->
            val state = stateMap[nutrient.id]!!
            val range = ranges[nutrient.id]
            Spacer(Modifier.height(12.dp))
            Text("${nutrient.name}: ${"%.1f".format(state.value)} ${nutrient.unit}")
            Slider(
                value = state.value,
                onValueChange = {
                    state.value = it
                    onRecipeChange(
                        NutrientRecipe(
                            crop = crop,
                            stage = stage,
                            values = stateMap.mapValues { it.value.value }
                        )
                    )
                },
                valueRange = 0f..(range?.max ?: 300f)
            )
            range?.let {
                Text("Rango recomendado: ${it.min}–${it.max} ${nutrient.unit}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
            }
        }
    }
}