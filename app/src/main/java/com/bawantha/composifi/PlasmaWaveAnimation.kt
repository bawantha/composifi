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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun PlasmaWaveAnimation(modifier: Modifier = Modifier) {
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
        val centerY = height / 2f
        val amplitude = height * 0.3f

        val points = 100
        val colors = listOf(
            Color(0xFFFF007F), // Neon Pink
            Color(0xFF00F0FF), // Neon Cyan
            Color(0xFF7FFF00), // Neon Green
            Color(0xFFFFEA00)  // Neon Yellow
        )

        for (i in 0 until 4) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (PI / 2).toFloat()
            val speedOffset = 1f + (i * 0.5f)

            path.reset()
            for (j in 0..points) {
                val x = (j.toFloat() / points) * width
                // Complex wave equation
                val y = centerY + amplitude * sin((j.toFloat() / points) * 4f * PI.toFloat() + time * speedOffset + phaseOffset) *
                                  sin(time * 0.5f + phaseOffset)

                if (j == 0) {
                    path.moveTo(x, y.toFloat())
                } else {
                    path.lineTo(x, y.toFloat())
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 6f + sin(time * 2f + phaseOffset).toFloat() * 3f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.8f
            )
        }
    }
}
