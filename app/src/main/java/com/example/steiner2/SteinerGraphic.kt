package com.example.steiner2
import com.example.steiner2.ui.theme.Steiner2Theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.math.sqrt

// --- PASO 1: Estructura de Datos y Normalización ---

/** Clase simple para guardar los tres porcentajes. */
data class TriplePercentages(val pA: Float, val pB: Float, val pC: Float)

/**
 * Normaliza tres porcentajes para que su suma sea 100.
 *
 * @param percentA Porcentaje del componente A.
 * @param percentB Porcentaje del componente B.
 * @param percentC Porcentaje del componente C.
 * @return Un objeto TriplePercentages con los valores normalizados,
 *         o valores por defecto si la suma inicial es 0 o negativa.
 */
fun normalizeTo100(percentA: Float, percentB: Float, percentC: Float): TriplePercentages {
    val sum = percentA + percentB + percentC

    return when {
        sum <= 0f -> {
            println("Error: La suma de porcentajes es $sum. No se puede normalizar. Usando valores por defecto.")
            TriplePercentages(33.333f, 33.333f, 33.334f)
        }
        Math.abs(sum - 100f) > 0.01f -> { // Usar tolerancia para flotantes
            val normA = (percentA / sum) * 100f
            val normB = (percentB / sum) * 100f
            val normC = (percentC / sum) * 100f
            // println("Normalizado: A=$normA, B=$normB, C=$normC (Suma original: $sum)")
            TriplePercentages(normA, normB, normC)
        }
        else -> {
            TriplePercentages(percentA, percentB, percentC)
        }
    }
}

// --- PASO 3: Definir Vértices Gráficos ---

/**
 * Calcula las coordenadas de los vértices de un triángulo equilátero
 * centrado dentro de las dimensiones y padding dados.
 *
 * @param canvasWidth El ancho total del área de dibujo.
 * @param canvasHeight La altura total del área de dibujo.
 * @param paddingPx El padding (en píxeles) a aplicar en todos los lados.
 * @return Un Triple con los Offsets de los vértices (A, B, C) o null si no hay espacio.
 *         Vértice A: Inferior izquierdo (100% A)
 *         Vértice B: Inferior derecho (100% B)
 *         Vértice C: Superior, centrado (100% C)
 */
fun calculateTriangleGraphicVertices(
    canvasWidth: Float,
    canvasHeight: Float,
    paddingPx: Float
): Triple<Offset, Offset, Offset>? {
    val usableWidth = canvasWidth - 2 * paddingPx
    val usableHeight = canvasHeight - 2 * paddingPx

    if (usableWidth <= 0 || usableHeight <= 0) {
        // println("Error: No hay espacio usable para dibujar el triángulo.")
        return null
    }

    val sideLengthFromHeight = (2f * usableHeight) / sqrt(3f)
    val sideLengthFromWidth = usableWidth
    val sideLength = min(sideLengthFromHeight, sideLengthFromWidth)

    if (sideLength <= 0) {
        // println("Error: Longitud de lado calculada es cero o negativa.")
        return null
    }

    val triangleActualHeight = (sqrt(3f) / 2f) * sideLength
    val halfSide = sideLength / 2f

    val centerX_usable = paddingPx + usableWidth / 2f
    val centerY_usable = paddingPx + usableHeight / 2f

    val topY = centerY_usable - triangleActualHeight / 2f
    val bottomY = centerY_usable + triangleActualHeight / 2f

    val vertexC = Offset(centerX_usable, topY)
    val vertexA = Offset(centerX_usable - halfSide, bottomY)
    val vertexB = Offset(centerX_usable + halfSide, bottomY)

    return Triple(vertexA, vertexB, vertexC)
}

// --- PASO 4: Adaptar Cálculo Baricéntrico para Offset ---

/**
 * Calcula las coordenadas 2D (Offset) de un punto dentro de un triángulo
 * dados sus porcentajes baricéntricos y los Offsets de los vértices del triángulo.
 *
 * @param normalizedPercentages Los porcentajes normalizados (A, B, C) que suman 100.
 * @param vertexA Offset del vértice que representa 100% del componente A.
 * @param vertexB Offset del vértice que representa 100% del componente B.
 * @param vertexC Offset del vértice que representa 100% del componente C.
 * @return El Offset (x, y) del punto dentro del triángulo.
 */
fun calculateBarycentricPointWithOffset(
    normalizedPercentages: TriplePercentages,
    vertexA: Offset,
    vertexB: Offset,
    vertexC: Offset
): Offset {
    val fracA = normalizedPercentages.pA / 100f
    val fracB = normalizedPercentages.pB / 100f
    val fracC = normalizedPercentages.pC / 100f

    val pointX = fracA * vertexA.x + fracB * vertexB.x + fracC * vertexC.x
    val pointY = fracA * vertexA.y + fracB * vertexB.y + fracC * vertexC.y

    return Offset(pointX, pointY)
}

// --- PASO 5: Composable Gráfico ---

// Constantes de estilo para el gráfico
private val DEFAULT_GRAPH_PADDING = 16.dp
private val TRIANGLE_BORDER_COLOR = Color.DarkGray
private val TRIANGLE_BORDER_WIDTH = 2.dp
private val DATA_POINT_COLOR = Color.Red
private val DATA_POINT_RADIUS = 6.dp

@Composable
fun SteinerTriangleGraphic(
    modifier: Modifier = Modifier,
    percentA: Float,
    percentB: Float,
    percentC: Float,
    graphPadding: Dp = DEFAULT_GRAPH_PADDING
) {
    // 1. Normalizar los datos de entrada
    val normalizedData = remember(percentA, percentB, percentC) {
        normalizeTo100(percentA, percentB, percentC)
    }

    // 2. Obtener densidad y convertir Dp a Px para el dibujo
    val density = LocalDensity.current
    val paddingPx = remember(graphPadding, density) { with(density) { graphPadding.toPx() } }
    val borderWidthPx = remember(TRIANGLE_BORDER_WIDTH, density) { with(density) { TRIANGLE_BORDER_WIDTH.toPx() } }
    val dataPointRadiusPx = remember(DATA_POINT_RADIUS, density) { with(density) { DATA_POINT_RADIUS.toPx() } }

    // 2.1 Hoistea aquí el Path, en contexto composable
    val trianglePath = remember { Path() }

    // 3. El Canvas para dibujar
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {
        val (vA, vB, vC) = calculateTriangleGraphicVertices(size.width, size.height, paddingPx)
            ?: return@Canvas

        // Calcular posición del punto
        val dataPointOffset = calculateBarycentricPointWithOffset(normalizedData, vA, vB, vC)

        // Reiniciar y construir el triángulo en el Path hoisteado
        trianglePath.reset()
        trianglePath.moveTo(vA.x, vA.y)
        trianglePath.lineTo(vB.x, vB.y)
        trianglePath.lineTo(vC.x, vC.y)
        trianglePath.close()

        // Dibujar contorno
        drawPath(
            path = trianglePath,
            color = TRIANGLE_BORDER_COLOR,
            style = Stroke(width = borderWidthPx)
        )

        // Dibujar punto de datos
        drawCircle(
            color = DATA_POINT_COLOR,
            radius = dataPointRadiusPx,
            center = dataPointOffset
        )
    }
}


// --- Previews ---
@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun SteinerTriangleGraphicPreview_Balanced() {
    // En una app real, envuelve esto en MaterialTheme o tu tema personalizado
    SteinerTriangleGraphic(
        percentA = 33.3f,
        percentB = 33.3f,
        percentC = 33.4f
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun SteinerTriangleGraphicPreview_HighA() {
    SteinerTriangleGraphic(
        percentA = 80f,
        percentB = 10f,
        percentC = 10f
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun SteinerTriangleGraphicPreview_HighB() {
    SteinerTriangleGraphic(
        percentA = 10f,
        percentB = 80f,
        percentC = 10f
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun SteinerTriangleGraphicPreview_HighC() {
    SteinerTriangleGraphic(
        percentA = 10f,
        percentB = 10f,
        percentC = 80f
    )
}