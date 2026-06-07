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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun NeonPulsingRingsAnimation(modifier: Modifier = Modifier) {
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
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxRadius = size.minDimension / 2f
        val ringCount = 5

        for (i in 0 until ringCount) {
            val phaseOffset = i * 0.5f
            val pulse = (sin(time * 2f + phaseOffset) + 1f) / 2f // 0 to 1

            val radius = maxRadius * (i + 1) / ringCount * (0.8f + 0.2f * pulse)
            val strokeWidth = 2f + 6f * pulse
            val alpha = 0.3f + 0.7f * pulse

            // Generate neon colors
            val hue = (time * 50f + i * 360f / ringCount) % 360f
            val color = Color.hsv(hue, 1f, 1f, alpha)

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
