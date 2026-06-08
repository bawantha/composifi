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
import kotlinx.coroutines.isActive

@Composable
fun RetroSynthwaveGridAnimation(modifier: Modifier = Modifier) {
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
        val horizonY = height * 0.4f
        val speed = 2f
        val offset = (time * speed) % 1f

        // Draw Sun
        val sunRadius = size.minDimension * 0.3f
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFD700), Color(0xFFFF007F)),
                startY = horizonY - sunRadius * 1.5f,
                endY = horizonY
            ),
            radius = sunRadius,
            center = Offset(width / 2f, horizonY - sunRadius * 0.2f)
        )

        // Draw grid below horizon
        val gridColor = Color(0xFF00F0FF)
        val numVerticalLines = 15

        // Vertical lines
        path.reset()
        for (i in -numVerticalLines..numVerticalLines) {
            val startX = width / 2f + (i * width * 0.05f)
            val endX = width / 2f + (i * width * 0.3f)

            drawLine(
                color = gridColor,
                start = Offset(startX, horizonY),
                end = Offset(endX, height),
                strokeWidth = 2f,
                alpha = 0.6f
            )
        }

        // Horizontal lines moving towards the viewer
        val numHorizontalLines = 8
        for (i in 0..numHorizontalLines) {
            val t = (i.toFloat() + offset) / numHorizontalLines
            val yPos = horizonY + (height - horizonY) * (t * t)
            val lineAlpha = (t * 0.8f).coerceIn(0f, 1f)

            drawLine(
                color = gridColor,
                start = Offset(0f, yPos),
                end = Offset(width, yPos),
                strokeWidth = 1f + t * 4f,
                alpha = lineAlpha
            )
        }
    }
}
