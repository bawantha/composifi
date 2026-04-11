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
fun SpirographAnimation(modifier: Modifier = Modifier) {
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
        val maxRadius = size.minDimension / 2f

        val points = 300
        val R = maxRadius * 0.6f
        val r = maxRadius * 0.3f
        val d = maxRadius * 0.4f * (sin(time).toFloat() + 1f) / 2f + maxRadius * 0.1f // Animate d

        val colors = listOf(
            Color(0xFF00FFCC), // Cyan
            Color(0xFFFF00FF), // Magenta
            Color(0xFFFFFF00)  // Yellow
        )

        for (i in 0 until 3) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (2f * PI.toFloat() / 3f)

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * 20f * PI.toFloat() + time * 0.5f

                val x = centerX + (R - r) * cos(t + phaseOffset) + d * cos((R - r) / r * (t + phaseOffset))
                val y = centerY + (R - r) * sin(t + phaseOffset) - d * sin((R - r) / r * (t + phaseOffset))

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
                    width = 3f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.7f
            )
        }
    }
}
