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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OrbitalRingsAnimation(modifier: Modifier = Modifier) {
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
        val center = Offset(width / 2f, height / 2f)
        val maxRadius = size.minDimension / 2f

        val colors = listOf(
            Color(0xFF00F0FF), // Neon Cyan
            Color(0xFFFF007F), // Neon Pink
            Color(0xFF7FFF00)  // Neon Green
        )

        for (i in 0 until 3) {
            val radius = maxRadius * (0.3f + 0.3f * i)
            val angle = time * (1f + i * 0.5f)

            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)

            // Draw orbit
            drawCircle(
                color = colors[i].copy(alpha = 0.3f),
                radius = radius,
                center = center,
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )

            // Draw orbiting planet
            drawCircle(
                color = colors[i],
                radius = 8f,
                center = Offset(x, y)
            )
        }

        // Draw center star
        drawCircle(
            color = Color(0xFFFFEA00),
            radius = 12f + 4f * sin(time * 4f),
            center = center
        )
    }
}
