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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InfinityLoopAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size.minDimension * 0.45f

        val points = 200
        path.reset()

        // Draw the Infinity path
        for (i in 0..points) {
            val t = (i.toFloat() / points) * 2f * PI.toFloat()
            // Infinity loop parameterization (Lemniscate of Bernoulli)
            val scale = 2f / (3f - cos(2f * t))
            val x = centerX + radius * scale * cos(t)
            val y = centerY + radius * scale * sin(2f * t) / 2f

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Base path
        drawPath(
            path = path,
            color = Color.DarkGray.copy(alpha = 0.3f),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )

        // Draw particles moving along the path
        val particleCount = 15
        for (i in 0 until particleCount) {
            val offsetT = (time * 0.5f + (i.toFloat() / particleCount) * 2f * PI.toFloat()) % (2f * PI.toFloat())
            val scale = 2f / (3f - cos(2f * offsetT))
            val px = centerX + radius * scale * cos(offsetT)
            val py = centerY + radius * scale * sin(2f * offsetT) / 2f

            // Particle color
            val hue = (offsetT / (2f * PI.toFloat()) * 360f + time * 50f) % 360f
            val color = Color.hsv(hue, 1f, 1f)

            // Draw particle
            drawCircle(
                color = color,
                radius = 8f + 4f * sin(time * 5f + i),
                center = Offset(px, py)
            )

            // Draw glowing effect around particle
            drawCircle(
                color = color.copy(alpha = 0.4f),
                radius = 16f + 8f * sin(time * 5f + i),
                center = Offset(px, py)
            )
        }
    }
}
