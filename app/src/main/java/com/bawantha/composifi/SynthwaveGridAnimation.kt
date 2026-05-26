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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive

@Composable
fun SynthwaveGridAnimation(modifier: Modifier = Modifier) {
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
        val horizonY = height * 0.45f

        // Draw Sun
        val sunRadius = size.minDimension * 0.35f
        val sunCenter = Offset(width / 2f, horizonY)

        val sunBrush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFF007F)), // Yellow to Neon Pink
            startY = horizonY - sunRadius,
            endY = horizonY + sunRadius
        )

        drawCircle(
            brush = sunBrush,
            radius = sunRadius,
            center = sunCenter
        )

        // Draw horizontal grid lines (moving towards viewer)
        val numLines = 15
        val speed = 1.5f
        val zOffset = (time * speed) % 1f

        val gridColor = Color(0xFF00F0FF) // Neon Cyan

        for (i in 0..numLines) {
            val z = i.toFloat() - zOffset
            if (z <= 0) continue

            // Perspective calculation
            val perspective = 1f / z
            val y = horizonY + (height - horizonY) * perspective * 0.5f

            if (y > height) continue

            val alpha = (1f - z / numLines).coerceIn(0f, 1f)

            drawLine(
                color = gridColor.copy(alpha = alpha),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f + 2f * perspective
            )
        }

        // Draw vertical grid lines (radiating from center perspective)
        val numVerticalLines = 15
        for (i in -numVerticalLines..numVerticalLines) {
            val xAtHorizon = width / 2f + i * 15f
            val xAtBottom = width / 2f + i * 150f

            // Fade out near the edges
            val alpha = (1f - kotlin.math.abs(i) / numVerticalLines.toFloat()).coerceIn(0f, 1f)

            drawLine(
                color = gridColor.copy(alpha = alpha * 0.8f),
                start = Offset(xAtHorizon, horizonY),
                end = Offset(xAtBottom, height),
                strokeWidth = 2f
            )
        }
    }
}
