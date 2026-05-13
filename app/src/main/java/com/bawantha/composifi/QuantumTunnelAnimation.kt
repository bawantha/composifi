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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun QuantumTunnelAnimation(modifier: Modifier = Modifier) {
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

        val maxRadius = sqrt((width / 2).pow(2) + (height / 2).pow(2))
        val rings = 15

        for (i in 0 until rings) {
            val ringOffset = (i.toFloat() / rings)
            val t = (time * 0.5f + ringOffset) % 1f

            val radius = maxRadius * t.pow(2)

            // Generate a color based on HSV
            val hue = (time * 50f + i * 20f) % 360f
            val alpha = if (t < 0.1f) t * 10f else if (t > 0.8f) (1f - t) * 5f else 1f

            val color = Color.hsv(hue = hue, saturation = 0.8f, value = 1f, alpha = alpha.coerceIn(0f, 1f))

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 4f + radius * 0.05f)
            )

            // Add some "quantum" particles on the ring
            val particles = 8
            for (j in 0 until particles) {
                val angle = (j.toFloat() / particles) * 2 * PI + (time * if (i % 2 == 0) 1f else -1f)
                val particleX = centerX + radius * cos(angle).toFloat()
                val particleY = centerY + radius * sin(angle).toFloat()

                drawCircle(
                    color = Color.White.copy(alpha = alpha.coerceIn(0f, 1f)),
                    radius = 2f + radius * 0.02f,
                    center = Offset(particleX, particleY)
                )
            }
        }
    }
}
