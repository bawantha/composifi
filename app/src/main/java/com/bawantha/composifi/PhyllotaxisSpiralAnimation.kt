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
import kotlin.math.sqrt

@Composable
fun PhyllotaxisSpiralAnimation(modifier: Modifier = Modifier) {
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

        val dotCount = 400
        val c = 4f
        val goldenAngle = 137.5f * (PI / 180f).toFloat()

        for (n in 0 until dotCount) {
            val a = n * goldenAngle + time
            val r = c * sqrt(n.toFloat())

            val x = centerX + r * cos(a)
            val y = centerY + r * sin(a)

            // Generate a color rotating around the color wheel based on time and index
            val hue = (n * 0.5f + time * 50f) % 360f
            val color = Color.hsv(hue = hue, saturation = 1f, value = 1f)

            // Make the dots fade out slightly towards the center
            val alpha = (n.toFloat() / dotCount).coerceIn(0.2f, 1f)

            drawCircle(
                color = color,
                radius = 2.5f,
                center = Offset(x, y),
                alpha = alpha
            )
        }
    }
}
