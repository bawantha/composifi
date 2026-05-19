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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PendulumWaveAnimation(modifier: Modifier = Modifier) {
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

        val pendulums = 15
        val originY = height * 0.1f
        val maxStringLength = height * 0.75f

        val timeToRead = time

        for (i in 0 until pendulums) {
            // The uncoupled pendulums have different frequencies.
            // Frequency f_i = f_0 + i * df
            val frequency = 0.5f + (i * 0.03f)
            val angle = sin(timeToRead * frequency * 2 * PI.toFloat()) * (PI / 4).toFloat()

            // Calculate length of the string based on its position in the wave to make it look slightly perspective
            val yOffset = originY + (i.toFloat() / pendulums) * (height * 0.1f)
            val stringLength = maxStringLength * (1f - (i.toFloat() / pendulums) * 0.15f)

            // X and Y position
            val x = width / 2f + sin(angle) * stringLength
            val y = yOffset + cos(angle) * stringLength

            val color = Color.hsv(
                hue = (i * 360f / pendulums + timeToRead * 20f) % 360f,
                saturation = 0.8f,
                value = 1.0f
            )

            // Draw line
            drawLine(
                color = Color.Gray.copy(alpha = 0.5f),
                start = Offset(width / 2f, yOffset),
                end = Offset(x, y),
                strokeWidth = 2f
            )

            // Draw bob
            val bobRadius = size.minDimension * 0.02f + (i.toFloat() / pendulums) * size.minDimension * 0.015f
            drawCircle(
                color = color,
                radius = bobRadius,
                center = Offset(x, y)
            )
        }
    }
}
