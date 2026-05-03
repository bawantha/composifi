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
fun InternetCoolAnimation(modifier: Modifier = Modifier) {
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
        val radius = size.minDimension * 0.4f

        val points = 300
        val rotations = 8f
        val colors = listOf(
            Color(0xFFFF007F), // Neon Pink
            Color(0xFF00F0FF), // Neon Cyan
            Color(0xFF7FFF00), // Neon Green
            Color(0xFFFFEA00)  // Neon Yellow
        )

        for (i in 0 until 4) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (PI / 4).toFloat()

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * rotations * 2f * PI.toFloat()

                // Dynamic Morphing Rose Curve / Mathematical Parametric Pattern
                val n = 4f + sin(time * 0.8f) * 2f
                val d = 3f + cos(time * 0.5f) * 1.5f
                val k = n / d

                // Calculate changing radius based on the math formula
                val rMath = radius * sin(k * t + time * 1.5f)

                // Add a secondary perturbation for extra "coolness"
                val rPerturb = radius * 0.2f * cos(5f * t - time * 2f)

                val r = rMath + rPerturb

                val x = centerX + r * cos(t + time * 0.3f + phaseOffset)
                val y = centerY + r * sin(t + time * 0.3f + phaseOffset)

                if (j == 0) {
                    path.moveTo(x.toFloat(), y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 3f + (sin(time * 3f + phaseOffset).toFloat() + 1f) * 1.5f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.85f
            )
        }
    }
}
