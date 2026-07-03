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
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SacredGeometryAnimation(modifier: Modifier = Modifier) {
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
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = size.minDimension / 2f * 0.9f

        val numShapes = 8

        for (i in 0 until numShapes) {
            val shapeRadius = maxRadius * (1f - i * 0.1f)
            val rotationAngle = time * 20f * if (i % 2 == 0) 1f else -1f
            val sides = 6

            path.reset()
            for (j in 0..sides) {
                val angle = (j.toFloat() / sides) * 2f * PI.toFloat()
                val x = center.x + shapeRadius * cos(angle)
                val y = center.y + shapeRadius * sin(angle)

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            val hue = (time * 50f + i * 360f / numShapes) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            rotate(degrees = rotationAngle + (i * 15f), pivot = center) {
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )

                for (j in 0 until sides) {
                    val angle = (j.toFloat() / sides) * 2f * PI.toFloat()
                    val x = center.x + shapeRadius * cos(angle)
                    val y = center.y + shapeRadius * sin(angle)
                    drawLine(
                        color = color.copy(alpha = 0.3f),
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}
