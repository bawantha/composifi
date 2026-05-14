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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KaleidoscopeAnimation(modifier: Modifier = Modifier) {
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
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height / 2f)
        val maxRadius = size.minDimension / 2f

        val segments = 6
        val anglePerSegment = 360f / segments

        val colors = listOf(
            Color(0xFFFF0000), // Red
            Color(0xFFFF7F00), // Orange
            Color(0xFFFFFF00), // Yellow
            Color(0xFF00FF00), // Green
            Color(0xFF0000FF), // Blue
            Color(0xFF4B0082)  // Indigo
        )

        for (i in 0 until segments) {
            rotate(degrees = i * anglePerSegment + time * 20f, pivot = center) {
                path.reset()

                val points = 30
                for (j in 0..points) {
                    val t = (j.toFloat() / points) * 2f * PI.toFloat()

                    val r = maxRadius * 0.8f * (0.5f + 0.5f * sin(3f * t + time))

                    // Parametric curve
                    val x = center.x + r * cos(t) * cos(time * 0.5f)
                    val y = center.y + r * sin(t) * sin(time * 0.7f)

                    if (j == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }

                drawPath(
                    path = path,
                    color = colors[i % colors.size],
                    style = Stroke(width = 3f, cap = StrokeCap.Round),
                    alpha = 0.8f
                )
            }
        }
    }
}
