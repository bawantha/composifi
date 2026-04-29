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
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Donut3DAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        val A = time * 1.5f
        val B = time * 1.0f
        val cosA = cos(A)
        val sinA = sin(A)
        val cosB = cos(B)
        val sinB = sin(B)

        // To handle z-buffering in Canvas, we collect points, sort by z, then draw
        val points = mutableListOf<Donut3DPoint>()

        var j = 0f
        while (j < 6.28f) { // phi
            var i = 0f
            while (i < 6.28f) { // theta
                val c = sin(i)
                val d = cos(j)
                val e = sinA
                val f = sin(j)
                val g = cosA

                val h = d + 2f
                // z = c * h * e + f * g + 5f
                val D = 1f / (c * h * e + f * g + 5f)
                val l = cos(i)
                val m = cosB
                val n = sinB
                val t = c * h * g - f * e

                // projected coordinates
                val x = centerX + width * 0.35f * D * (l * h * m - t * n)
                val y = centerY + width * 0.35f * D * (l * h * n + t * m)

                // normal
                val N = 8f * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n)

                if (N > 0f) {
                    points.add(Donut3DPoint(x, y, D, N))
                }

                i += 0.1f // increase for more density
            }
            j += 0.1f // increase for more density
        }

        // Sort by D (1/z) ascending. Smaller D means larger z (further away).
        points.sortBy { it.D }

        for (point in points) {
            val luminance = (point.N / 8f).coerceIn(0f, 1f)
            drawCircle(
                color = Color(0xFF00FF00).copy(alpha = 0.3f + 0.7f * luminance),
                radius = 1f + 2f * luminance,
                center = Offset(point.x, point.y)
            )
        }
    }
}

private data class Donut3DPoint(val x: Float, val y: Float, val D: Float, val N: Float)
