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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TunnelVisionAnimation(modifier: Modifier = Modifier) {
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

        val maxRadius = size.maxDimension * 1.5f
        val numHexagons = 15

        // Use time to offset the drawing of hexagons
        for (i in 0 until numHexagons) {
            val progress = (i.toFloat() / numHexagons + time * 0.5f) % 1f
            val radius = maxRadius * progress * progress * progress // exponential growth for 3D depth effect

            val alpha = if (progress < 0.1f) {
                progress / 0.1f
            } else if (progress > 0.8f) {
                (1f - progress) / 0.2f
            } else {
                1f
            }

            // Color shifts over time and depth
            val hue = (time * 50f + i * 20f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f).copy(alpha = alpha)

            val strokeWidth = 2f + 10f * progress

            path.reset()
            for (j in 0..6) {
                // adding a twist
                val angle = j * PI.toFloat() / 3f + time * 0.2f + progress * PI.toFloat()
                val x = centerX + radius * cos(angle)
                val y = centerY + radius * sin(angle)

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
