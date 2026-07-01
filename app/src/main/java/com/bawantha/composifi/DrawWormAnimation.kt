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
fun DrawWormAnimation(modifier: Modifier = Modifier) {
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

        val points = 60
        val colors = listOf(
            Color(0xFFFF3366),
            Color(0xFF33CCFF),
            Color(0xFF33FF66),
            Color(0xFFFFCC00)
        )

        for (i in 0 until 3) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (PI / 3).toFloat()

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * 2f * PI.toFloat()

                // Worm-like movement using compound trigonometric functions
                val x = centerX + radius * sin(t + time * 1.5f + phaseOffset) * cos(t * 2f + time)
                val y = centerY + radius * cos(t + time * 2f + phaseOffset) * sin(t * 1.5f + time * 0.5f)

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
                    width = 6f + (sin(time * 3f + phaseOffset).toFloat() + 1f) * 3f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.9f
            )
        }
    }
}
