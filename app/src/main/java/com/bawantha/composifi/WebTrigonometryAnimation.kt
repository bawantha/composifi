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
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WebTrigonometryAnimation(modifier: Modifier = Modifier) {
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
        val numPoints = 80
        val rings = 3

        for (r in 1..rings) {
            val radius = maxRadius * (r.toFloat() / rings)

            for (i in 0 until numPoints) {
                val progress = i.toFloat() / numPoints
                val angle1 = progress * 2f * PI.toFloat() + time * (0.5f + r * 0.2f)

                val nextProgress = ((i + 1) % numPoints).toFloat() / numPoints
                val angle2 = nextProgress * 2f * PI.toFloat() + time * (0.5f + r * 0.2f)

                // Add some trigonometric wave effect to radius
                val wave1 = sin(angle1 * 3f + time * 2f) * (maxRadius * 0.15f)
                val wave2 = sin(angle2 * 3f + time * 2f) * (maxRadius * 0.15f)

                val currentR1 = radius + wave1
                val currentR2 = radius + wave2

                val x1 = centerX + currentR1 * cos(angle1)
                val y1 = centerY + currentR1 * sin(angle1)

                val x2 = centerX + currentR2 * cos(angle2)
                val y2 = centerY + currentR2 * sin(angle2)

                val hue = (time * 40f + r * 120f + progress * 360f) % 360f
                val color = Color.hsv(hue, 0.8f, 1f, 0.7f)

                drawLine(
                    color = color,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = 3f
                )

                // Draw connecting lines between rings to create a web effect
                if (r < rings) {
                    val innerRadius = maxRadius * (r.toFloat() / rings)
                    val outerRadius = maxRadius * ((r + 1).toFloat() / rings)

                    val innerWave = sin(angle1 * 3f + time * 2f) * (maxRadius * 0.15f)
                    val outerWave = sin(angle1 * 3f + time * 2f) * (maxRadius * 0.15f)

                    val innerR = innerRadius + innerWave
                    val outerR = outerRadius + outerWave

                    val innerX = centerX + innerR * cos(angle1)
                    val innerY = centerY + innerR * sin(angle1)

                    val outerX = centerX + outerR * cos(angle1)
                    val outerY = centerY + outerR * sin(angle1)

                    drawLine(
                        color = Color.hsv(hue, 0.8f, 1f, 0.3f),
                        start = Offset(innerX, innerY),
                        end = Offset(outerX, outerY),
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}
