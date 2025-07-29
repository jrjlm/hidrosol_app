package com.example.steiner2.model

enum class NutrientCategory { MACRO, MICRO }

data class Nutrient(
    val id: String,
    val name: String,
    val unit: String,
    val category: NutrientCategory
)

data class RecommendedRange(
    val nutrientId: String,
    val crop: String,
    val stage: String,
    val min: Float,
    val max: Float
)

data class NutrientRecipe(
    val crop: String,
    val stage: String,
    val values: Map<String, Float>
)

object NutrientRepository {
    val nutrients = listOf(
        Nutrient("N",  "Nitrógeno",       "mg/L", NutrientCategory.MACRO),
        Nutrient("P",  "Fósforo (P₂O₅)",   "mg/L", NutrientCategory.MACRO),
        Nutrient("K",  "Potasio",          "mg/L", NutrientCategory.MACRO),
        Nutrient("Ca", "Calcio",           "mg/L", NutrientCategory.MACRO),
        Nutrient("Mg", "Magnesio",         "mg/L", NutrientCategory.MACRO),
        Nutrient("S",  "Azufre",           "mg/L", NutrientCategory.MACRO),
        Nutrient("Fe", "Hierro",           "mg/L", NutrientCategory.MICRO),
        Nutrient("Mn", "Manganeso",        "mg/L", NutrientCategory.MICRO),
        Nutrient("Zn", "Zinc",             "mg/L", NutrientCategory.MICRO),
        Nutrient("B",  "Boro",             "mg/L", NutrientCategory.MICRO),
        Nutrient("Cu", "Cobre",            "mg/L", NutrientCategory.MICRO),
        Nutrient("Mo", "Molibdeno",        "mg/L", NutrientCategory.MICRO)
    )

    val recommendedRanges = listOf(
        RecommendedRange("N", "Lechuga",     "Vegetativo", 100f, 150f),
        RecommendedRange("P", "Lechuga",     "Vegetativo",  30f,  50f),
        RecommendedRange("K", "Lechuga",     "Vegetativo", 100f, 200f),
        RecommendedRange("Ca","Lechuga",     "Vegetativo", 120f, 150f),
        RecommendedRange("Fe","Tomate",      "Floración",  2.0f, 3.0f),
        RecommendedRange("K", "Tomate",      "Floración", 150f, 250f)
    )

    fun getRanges(crop: String, stage: String): List<RecommendedRange> =
        recommendedRanges.filter { it.crop == crop && it.stage == stage }
}