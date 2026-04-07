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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AtomAnimation(modifier: Modifier = Modifier) {
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
        val radiusX = size.minDimension * 0.4f
        val radiusY = size.minDimension * 0.15f

        // Draw nucleus
        drawCircle(
            color = Color(0xFFFF5252),
            radius = size.minDimension * 0.08f,
            center = Offset(centerX, centerY)
        )

        val orbitalColors = listOf(Color(0xFF448AFF), Color(0xFF69F0AE), Color(0xFFFFD740))
        val angles = listOf(0f, 60f, 120f)

        for (i in 0 until 3) {
            val angle = angles[i]
            val color = orbitalColors[i]

            withTransform({
                translate(left = centerX, top = centerY)
                rotate(angle)
                translate(left = -centerX, top = -centerY)
            }) {
                // Draw orbital path
                drawOval(
                    color = color.copy(alpha = 0.5f),
                    topLeft = Offset(centerX - radiusX, centerY - radiusY),
                    size = Size(radiusX * 2, radiusY * 2),
                    style = Stroke(width = 4f)
                )

                // Calculate electron position
                // The electron moves along the ellipse over time.
                val speed = 3f
                val t = time * speed + i * (PI.toFloat() * 2f / 3f)
                val ex = centerX + radiusX * cos(t)
                val ey = centerY + radiusY * sin(t)

                // Draw electron
                drawCircle(
                    color = color,
                    radius = size.minDimension * 0.05f,
                    center = Offset(ex, ey)
                )
            }
        }
    }
}
