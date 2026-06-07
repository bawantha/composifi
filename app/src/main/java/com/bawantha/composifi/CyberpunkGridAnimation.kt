package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

        // Draw the background horizon
        drawRect(
            color = Color(0xFF0F0F1A),
            size = size
        )

        // Draw sun
        val sunCenter = Offset(width / 2f, height * 0.4f)
        val sunRadius = size.minDimension * 0.25f

        // Draw glowing sun
        drawCircle(
            color = Color(0xFFFF0055).copy(alpha = 0.3f),
            radius = sunRadius * 1.2f,
            center = sunCenter
        )
        drawCircle(
            color = Color(0xFFFF0055),
            radius = sunRadius,
            center = sunCenter
        )

        // Horizontal line for horizon
        val horizonY = height * 0.4f
        val gridColor = Color(0xFF00FFFF)

        // Vertical lines radiating from the vanishing point
        val vanishingPoint = Offset(width / 2f, horizonY)
        val numVerticalLines = 15

        for (i in 0..numVerticalLines) {
            val xOffset = (i.toFloat() / numVerticalLines - 0.5f) * width * 4f
            drawLine(
                color = gridColor,
                start = vanishingPoint,
                end = Offset(width / 2f + xOffset, height),
                strokeWidth = 2f,
                alpha = 0.5f
            )
        }

        // Horizontal lines moving towards viewer
        val numHorizontalLines = 10
        val speed = 0.5f // lines per second
        val offsetTime = (time * speed) % 1f

        for (i in 0..numHorizontalLines) {
            // Power curve to simulate perspective (lines get further apart at the bottom)
            val normalizedY = (i.toFloat() + offsetTime) / numHorizontalLines
            if (normalizedY > 1f) continue

            val perspectiveY = horizonY + (height - horizonY) * (normalizedY * normalizedY * normalizedY)

            drawLine(
                color = gridColor,
                start = Offset(0f, perspectiveY),
                end = Offset(width, perspectiveY),
                strokeWidth = 2f + 3f * normalizedY,
                alpha = 0.8f * normalizedY
            )
        }
    }
}
