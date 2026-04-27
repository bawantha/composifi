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
import kotlin.math.abs

@Composable
fun RetroWaveGridAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val gridPath = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val horizonY = height * 0.4f

        // Draw background (dark sky to grid area)
        drawRect(
            color = Color(0xFF0F0F1A),
            size = size
        )

        // Draw Sun at horizon
        val sunRadius = size.minDimension * 0.3f
        val sunCenter = Offset(width / 2f, horizonY)
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFEA00), Color(0xFFFF007F)), // Yellow to Neon Pink
                startY = horizonY - sunRadius,
                endY = horizonY + sunRadius
            ),
            radius = sunRadius,
            center = sunCenter
        )

        // Draw sun "stripes" (retro style)
        val numStripes = 6
        for (i in 0 until numStripes) {
            val stripeHeight = sunRadius * 0.15f * (i + 1) / numStripes
            val stripeY = horizonY + sunRadius - sunRadius * 0.8f * (i + 1) / numStripes
            drawRect(
                color = Color(0xFF0F0F1A),
                topLeft = Offset(width / 2f - sunRadius, stripeY),
                size = androidx.compose.ui.geometry.Size(sunRadius * 2f, stripeHeight)
            )
        }

        // Draw Moving Grid (Perspective)
        gridPath.reset()
        val gridColor = Color(0xFF00F0FF) // Neon Cyan

        // Horizontal lines (moving towards viewer)
        val speed = 1.5f
        val numHLines = 12
        for (i in 0..numHLines) {
            val phase = (time * speed + i.toFloat() / numHLines) % 1f
            val yProgress = phase * phase * phase // Non-linear spacing for perspective
            val y = horizonY + (height - horizonY) * yProgress

            val alpha = if (yProgress < 0.05f) yProgress / 0.05f else 1f

            drawLine(
                color = gridColor.copy(alpha = alpha * 0.8f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 2f + yProgress * 4f // Thicker as it gets closer
            )
        }

        // Vertical lines (converging to center horizon)
        val numVLines = 20
        val centerVanishingPoint = width / 2f
        for (i in -numVLines..numVLines) {
            val xOffset = i * width * 0.15f
            val startX = centerVanishingPoint
            val endX = centerVanishingPoint + xOffset

            drawLine(
                color = gridColor.copy(alpha = 0.5f),
                start = Offset(startX, horizonY),
                end = Offset(endX, height),
                strokeWidth = 2f
            )
        }
    }
}
