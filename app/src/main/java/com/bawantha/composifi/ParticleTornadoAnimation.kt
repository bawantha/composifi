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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ParticleTornadoAnimation(modifier: Modifier = Modifier) {
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

        val numParticles = 200
        val tornadoHeight = size.height * 0.8f

        for (i in 0 until numParticles) {
            // Calculate height position (y) mapped to progress [0, 1]
            val progress = (i.toFloat() / numParticles)
            // Reverse so top is wider and bottom is narrower
            val yOffset = -tornadoHeight / 2f + tornadoHeight * progress

            // Add a sine wave oscillation to the radius to make it dynamic over time
            val dynamicRadius = maxRadius * progress * (0.5f + 0.5f * sin(time * 2f + progress * 5f))

            // Rotation speed changes based on height
            val speed = 3f + (1f - progress) * 5f

            // Angle around the center
            val angle = time * speed + i * 0.5f

            val x = centerX + dynamicRadius * cos(angle)
            val y = centerY + yOffset

            // Adding a little bit of depth / z-index scaling
            val z = sin(angle)
            val particleSize = 2f + (z + 1f) * 3f

            val alpha = 0.3f + (z + 1f) * 0.35f

            // Hue shifts from bottom to top and over time
            val hue = (progress * 360f + time * 50f) % 360f

            drawCircle(
                color = Color.hsv(hue, 0.8f, 1f, alpha),
                radius = particleSize,
                center = Offset(x, y)
            )
        }
    }
}
