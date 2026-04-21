package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeonHexagonAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxRadius = size.minDimension * 0.45f

        val numHexagons = 6
        val colors = listOf(
            Color(0xFF00FFFF), // Cyan
            Color(0xFFFF00FF), // Magenta
            Color(0xFFFFFF00), // Yellow
            Color(0xFF00FF00), // Green
            Color(0xFFFF0000), // Red
            Color(0xFF0000FF)  // Blue
        )

        for (i in 0 until numHexagons) {
            path.reset()
            val radiusOffset = sin(time * 3f + i).toFloat() * 15f
            val radius = maxRadius * ((i + 1f) / numHexagons) + radiusOffset

            // Ensure radius doesn't go negative or look weird
            val safeRadius = if (radius < 0f) 0f else radius

            val rotation = time * (if (i % 2 == 0) 1f else -1f) * 0.5f

            for (j in 0..6) {
                val angle = rotation + j * (PI / 3).toFloat()
                val x = cx + safeRadius * cos(angle)
                val y = cy + safeRadius * sin(angle)

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = colors[i % colors.size],
                style = Stroke(
                    width = 4f + (sin(time * 5f + i).toFloat() + 1f) * 2f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                ),
                alpha = 0.8f + 0.2f * sin(time * 4f + i).toFloat()
            )
        }
    }
}
