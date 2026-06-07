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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun AudioVisualizerAnimation(modifier: Modifier = Modifier) {
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
        val barCount = 20
        val spacing = size.width * 0.02f
        val barWidth = (size.width - spacing * (barCount - 1)) / barCount
        val maxHeight = size.height

        for (i in 0 until barCount) {
            // Calculate a wave-like height based on time and bar index
            val phase = i * 0.5f
            val heightMultiplier = (sin(time * 3f + phase) + sin(time * 5f - phase) + 2f) / 4f
            val currentHeight = maxHeight * heightMultiplier.coerceIn(0.1f, 1f)

            // Shift hue based on time and index
            val hue = (time * 50f + i * 15f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            val xOffset = i * (barWidth + spacing)
            val yOffset = maxHeight - currentHeight // Draw from bottom up

            drawRoundRect(
                color = color,
                topLeft = Offset(xOffset, yOffset),
                size = Size(barWidth, currentHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            )
        }
    }
}
