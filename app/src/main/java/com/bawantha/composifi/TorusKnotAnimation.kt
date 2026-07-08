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
fun TorusKnotAnimation(modifier: Modifier = Modifier) {
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
        val scale = size.minDimension * 0.2f

        val p = 2
        val q = 3
        val points = 300

        for (i in 0 until points) {
            val t = (i.toFloat() / points) * 2f * PI.toFloat() + time

            val r = cos(q * t) + 2f
            val x = r * cos(p * t) * scale + centerX
            val y = r * sin(p * t) * scale + centerY

            // Adding a simple 3D effect by modulating radius and color based on z-like value
            val z = sin(q * t) * scale
            val pointScale = (z + scale) / (2f * scale)
            val radius = 2f + pointScale * 5f

            val hue = (t / (2f * PI.toFloat()) * 360f) % 360f
            val color = Color.hsv(hue, 1f, 1f)

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(x, y),
                alpha = 0.8f
            )
        }
    }
}
