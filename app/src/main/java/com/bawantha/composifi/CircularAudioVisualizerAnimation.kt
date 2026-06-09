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
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.abs

@Composable
fun CircularAudioVisualizerAnimation(modifier: Modifier = Modifier) {
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
        val baseRadius = size.minDimension * 0.2f
        val maxBarHeight = size.minDimension * 0.25f

        // Pulsing center circle
        val pulseScale = 1f + 0.15f * sin(time * 3f)
        val centerRadius = baseRadius * pulseScale

        drawCircle(
            color = Color(0xFF00FFCC).copy(alpha = 0.2f),
            radius = centerRadius * 1.5f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color(0xFF00FFCC),
            radius = centerRadius,
            center = Offset(centerX, centerY)
        )

        // Radial bars
        val numBars = 60
        for (i in 0 until numBars) {
            val angle = (i.toFloat() / numBars) * 2f * PI.toFloat()

            // Generate pseudo-random frequencies based on bar index and time
            val freq1 = sin(time * 5f + i * 0.5f)
            val freq2 = cos(time * 3f + i * 0.2f)
            val freq3 = sin(time * 8f - i * 0.8f)

            val amplitude = abs(freq1 * 0.5f + freq2 * 0.3f + freq3 * 0.2f)

            val barHeight = amplitude * maxBarHeight

            val innerX = centerX + cos(angle) * (centerRadius + 4f)
            val innerY = centerY + sin(angle) * (centerRadius + 4f)

            val outerX = centerX + cos(angle) * (centerRadius + 4f + barHeight)
            val outerY = centerY + sin(angle) * (centerRadius + 4f + barHeight)

            // Color gradient based on amplitude
            val r = (0.0f + amplitude * 1.0f).coerceIn(0f, 1f)
            val g = (1.0f - amplitude * 0.5f).coerceIn(0f, 1f)
            val b = (0.8f - amplitude * 0.8f).coerceIn(0f, 1f)

            drawLine(
                color = Color(r, g, b, 1.0f),
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
    }
}
