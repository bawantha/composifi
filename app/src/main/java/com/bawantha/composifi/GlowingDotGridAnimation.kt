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
import kotlin.math.sin

@Composable
fun GlowingDotGridAnimation(modifier: Modifier = Modifier) {
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

        val cols = 8
        val rows = 8
        val cellWidth = width / cols
        val cellHeight = height / rows

        for (i in 0 until cols) {
            for (j in 0 until rows) {
                val x = i * cellWidth + cellWidth / 2
                val y = j * cellHeight + cellHeight / 2

                // Calculate distance from center to create a wave effect
                val dx = x - width / 2
                val dy = y - height / 2
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                // Radius changes based on time and distance
                val maxRadius = minOf(cellWidth, cellHeight) / 2f * 0.8f
                val phase = distance * 0.05f - time * 3f
                val r = (sin(phase) + 1f) / 2f * maxRadius

                // Color based on position and time
                val hue = (time * 50f + i * 10f + j * 10f) % 360f
                val color = Color.hsv(hue, 0.8f, 1f)

                drawCircle(
                    color = color,
                    radius = r,
                    center = Offset(x, y),
                    alpha = (sin(phase) + 1f) / 2f * 0.8f + 0.2f
                )
            }
        }
    }
}
