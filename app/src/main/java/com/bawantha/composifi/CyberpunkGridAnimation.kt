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
import kotlinx.coroutines.isActive

@Composable
fun CyberpunkGridAnimation(modifier: Modifier = Modifier) {
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
        val centerY = height * 0.4f // Horizon line slightly above center

        // Draw sky / background glow
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0F0C29),
                    Color(0xFF302B63),
                    Color(0xFF24243E)
                )
            ),
            size = size
        )

        // Draw the neon sun at the horizon
        val sunRadius = size.minDimension * 0.3f
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFF007F), // Neon Pink
                    Color(0xFFFFEA00)  // Neon Yellow
                ),
                startY = centerY - sunRadius,
                endY = centerY + sunRadius
            ),
            radius = sunRadius,
            center = Offset(centerX, centerY)
        )

        // Sun grid lines (horizontal cuts in the sun)
        for (i in 0 until 5) {
            val cutY = centerY + sunRadius * 0.2f + i * (sunRadius * 0.2f)
            drawRect(
                color = Color(0xFF24243E), // Match background color
                topLeft = Offset(centerX - sunRadius, cutY),
                size = androidx.compose.ui.geometry.Size(sunRadius * 2, sunRadius * 0.05f * (i + 1))
            )
        }

        // Draw horizon line
        drawLine(
            color = Color(0xFF00F0FF), // Neon Cyan
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = 4f
        )

        // 3D Perspective Grid
        val gridColor = Color(0xFFFF007F) // Neon Pink
        val speed = 2f
        val offset = (time * speed) % 1f
        val lines = 20

        // Vertical perspective lines
        for (i in -lines..lines) {
            val startX = centerX + i * 20f
            val endX = centerX + i * 150f
            drawLine(
                color = gridColor.copy(alpha = 0.6f),
                start = Offset(startX, centerY),
                end = Offset(endX, height),
                strokeWidth = 2f
            )
        }

        // Horizontal moving lines
        for (i in 0 until lines) {
            val progress = (i.toFloat() + offset) / lines.toFloat()
            // Nonlinear mapping for perspective depth
            val depth = progress * progress * progress
            val y = centerY + (height - centerY) * depth

            val alpha = (depth * 1.5f).coerceIn(0f, 1f)

            drawLine(
                color = gridColor.copy(alpha = alpha),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 2f + depth * 3f
            )
        }
    }
}
