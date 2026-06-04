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

@Composable
fun CoolAnimationEffects(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val particleCount = 100

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        val maxRadius = size.minDimension / 2f * 0.8f

        for (i in 0 until particleCount) {
            val angle = (i.toFloat() / particleCount) * 2f * PI.toFloat() + time * 0.5f
            val radiusOffset = sin(time * 2f + i.toFloat() * 0.1f) * 20f
            val currentRadius = maxRadius * 0.5f + radiusOffset

            val x = centerX + currentRadius * cos(angle)
            val y = centerY + currentRadius * sin(angle)

            val alpha = (sin(time * 3f + i.toFloat() * 0.2f) + 1f) / 2f

            val hue = (time * 50f + i * 3.6f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f, alpha)

            drawCircle(
                color = color,
                radius = 4f + 3f * alpha,
                center = Offset(x, y)
            )

            // Draw connecting lines to center
            drawLine(
                color = color.copy(alpha = alpha * 0.3f),
                start = Offset(centerX, centerY),
                end = Offset(x, y),
                strokeWidth = 1f,
                cap = StrokeCap.Round
            )
        }
    }
}
