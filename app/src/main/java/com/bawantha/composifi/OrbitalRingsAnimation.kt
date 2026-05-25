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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OrbitalRingsAnimation(modifier: Modifier = Modifier) {
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
        val maxRadius = size.minDimension * 0.45f

        val numRings = 5

        for (i in 0 until numRings) {
            val progress = (time * (1f + i * 0.2f)) % (2f * PI.toFloat())
            val ringRadius = maxRadius * (0.4f + 0.6f * (i.toFloat() / numRings))
            val hue = (time * 50f + i * 360f / numRings) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            rotate(degrees = (time * 20f * if (i % 2 == 0) 1f else -1f) + i * 45f) {
                // Draw the orbital ring (ellipse)
                drawOval(
                    color = color.copy(alpha = 0.3f),
                    topLeft = Offset(centerX - ringRadius, centerY - ringRadius * 0.3f),
                    size = Size(ringRadius * 2f, ringRadius * 0.6f),
                    style = Stroke(width = 2f)
                )

                // Calculate position for the orbiting body
                val angle = progress + i * PI.toFloat() / 2f
                val orbitX = centerX + ringRadius * cos(angle)
                val orbitY = centerY + ringRadius * 0.3f * sin(angle)

                // Draw the orbiting body
                drawCircle(
                    color = color,
                    radius = 4f + (i * 1.5f),
                    center = Offset(orbitX, orbitY)
                )

                // Draw a small glow around the body
                drawCircle(
                    color = color.copy(alpha = 0.5f),
                    radius = 8f + (i * 2f),
                    center = Offset(orbitX, orbitY)
                )
            }
        }
    }
}
