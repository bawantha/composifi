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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlasmaOrbAnimation(modifier: Modifier = Modifier) {
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

        val points = 100
        val colors = listOf(
            Color(0xFFFF007F), // Neon Pink
            Color(0xFF00F0FF), // Neon Cyan
            Color(0xFF8A2BE2), // Blue Violet
            Color(0xFF00FF7F)  // Spring Green
        )

        for (i in 0 until 6) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (PI / 3).toFloat()

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * 2f * PI.toFloat()

                val currentRadius = radius + sin(t * 3 + time * 4 + phaseOffset) * 10f + cos(t * 5 + time * 2) * 5f

                val x = centerX + currentRadius * cos(t + time + phaseOffset)
                val y = centerY + currentRadius * sin(t + time + phaseOffset)

                if (j == 0) {
                    path.moveTo(x.toFloat(), y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            path.close()

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 3f + (sin(time * 3f + phaseOffset).toFloat() + 1f) * 2f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.6f,
                blendMode = BlendMode.Screen
            )
        }

        // Inner glowing core
        drawCircle(
            color = Color.White,
            radius = radius * 0.2f + sin(time * 5).toFloat() * 5f,
            center = Offset(centerX, centerY),
            alpha = 0.8f,
            blendMode = BlendMode.Screen
        )
    }
}
