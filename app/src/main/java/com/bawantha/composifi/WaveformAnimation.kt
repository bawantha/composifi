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
import kotlin.math.sin

@Composable
fun WaveformAnimation(modifier: Modifier = Modifier) {
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
        val points = 100

        val colors = listOf(
            Color(0xFFff3366),
            Color(0xFF20d2d2),
            Color(0xFFffaa00)
        )

        for (i in 0 until 3) {
            path.reset()
            val phaseOffset = i * (PI.toFloat() * 2f / 3f)
            val amplitude = height * 0.3f

            for (j in 0..points) {
                val x = (j.toFloat() / points) * width
                val normalizedX = (j.toFloat() / points) * 2f * PI.toFloat()

                val yOffset = sin(normalizedX * 2f + time * 3f + phaseOffset) *
                              sin(normalizedX + time * 2f) * amplitude

                val y = centerY + yOffset

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = colors[i % colors.size],
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.8f
            )
        }
    }
}
