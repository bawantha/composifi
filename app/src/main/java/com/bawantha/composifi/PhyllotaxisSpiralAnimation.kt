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

        val totalPoints = 500
        val goldenAngle = 137.5f * (PI / 180f).toFloat()
        val rotationOffset = time * 0.5f

        // Scaling factor c
        val c = size.minDimension * 0.02f

        for (n in 0 until totalPoints) {
            val a = n * goldenAngle + rotationOffset
            val r = c * sqrt(n.toFloat())

            // Pulsing effect
            val currentR = r + sin(time * 3f + n * 0.05f).toFloat() * 5f

            val x = currentR * cos(a) + centerX
            val y = currentR * sin(a) + centerY

            val hue = (n * 1.5f + time * 50f) % 360f

            drawCircle(
                color = Color.hsv(hue = hue, saturation = 1f, value = 1f),
                radius = 2f + (n.toFloat() / totalPoints) * 4f,
                center = Offset(x, y)
            )
        }
    }
}