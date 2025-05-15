package com.example.steiner2.data



import com.example.steiner2.model.Nutrient
import com.example.steiner2.model.NutrientCategory
import com.example.steiner2.model.RecommendedRange

/**
 * Fuente de datos en memoria para nutrientes y rangos.
 */
object NutrientRepository {
    // Definición de nutrientes
    val nutrients = listOf(
        Nutrient("N",  "Nitrógeno",       "mg/L", NutrientCategory.MACRO),
        Nutrient("P",  "Fósforo (P₂O₅)",   "mg/L", NutrientCategory.MACRO),
        Nutrient("K",  "Potasio",          "mg/L", NutrientCategory.MACRO),
        Nutrient("Ca", "Calcio",           "mg/L", NutrientCategory.MACRO),
        Nutrient("Mg", "Magnesio",         "mg/L", NutrientCategory.MACRO),
        Nutrient("S",  "Azufre",           "mg/L", NutrientCategory.MACRO),
        // micronutrientes
        Nutrient("Fe", "Hierro",           "mg/L", NutrientCategory.MICRO),
        Nutrient("Mn", "Manganeso",        "mg/L", NutrientCategory.MICRO),
        Nutrient("Zn", "Zinc",             "mg/L", NutrientCategory.MICRO),
        Nutrient("B",  "Boro",             "mg/L", NutrientCategory.MICRO),
        Nutrient("Cu", "Cobre",            "mg/L", NutrientCategory.MICRO),
        Nutrient("Mo", "Molibdeno",        "mg/L", NutrientCategory.MICRO)
    )

    // Rangos recomendados de ejemplo para Lechuga
    private val recommendedRanges = listOf(
        RecommendedRange("N",  "Lechuga", "Vegetativo", 100f, 150f),
        RecommendedRange("P",  "Lechuga", "Vegetativo",  30f,  50f),
        RecommendedRange("K",  "Lechuga", "Vegetativo", 100f, 200f),
        RecommendedRange("Ca", "Lechuga", "Vegetativo", 120f, 150f),
        RecommendedRange("Mg", "Lechuga", "Vegetativo",  40f,  60f),
        RecommendedRange("S",  "Lechuga", "Vegetativo",  50f,  70f)
        // Agrega más para otros cultivos/etapas
    )

    /**
     * Recupera rangos para un cultivo y etapa.
     */
    fun getRanges(crop: String, stage: String): List<RecommendedRange> =
        recommendedRanges.filter { it.crop == crop && it.stage == stage }
}