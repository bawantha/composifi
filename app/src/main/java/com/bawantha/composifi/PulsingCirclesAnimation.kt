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
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun PulsingCirclesAnimation(modifier: Modifier = Modifier) {
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
        val baseRadius = size.minDimension * 0.1f

        val numCircles = 8
        for (i in 0 until numCircles) {
            val phaseOffset = i * (PI / 4).toFloat()
            val animatedRadius = baseRadius + (baseRadius * 0.5f * sin(time * 3f + phaseOffset))
            val expandedRadius = baseRadius * (i + 1) * 0.6f + animatedRadius

            // Calculate color with an animated hue shift based on time and position
            val hue = (time * 50f + i * 360f / numCircles) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            val strokeWidth = 2f + (2f * sin(time * 2f + phaseOffset) + 2f)

            drawCircle(
                color = color,
                radius = expandedRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}
