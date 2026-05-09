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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KaleidoscopeAnimation(modifier: Modifier = Modifier) {
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

        val layers = 6
        val sides = 8

        for (layer in 1..layers) {
            val layerRadius = maxRadius * (layer.toFloat() / layers)
            val rotationSpeed = if (layer % 2 == 0) 1f else -1f
            val rotationAngle = time * 30f * rotationSpeed + (layer * 15f)

            rotate(degrees = rotationAngle, pivot = Offset(centerX, centerY)) {
                path.reset()

                for (i in 0..sides) {
                    val angle = i * (2 * PI / sides)
                    val x = centerX + layerRadius * cos(angle).toFloat()
                    val y = centerY + layerRadius * sin(angle).toFloat()

                    if (i == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }

                val hue = (time * 50f + layer * (360f / layers)) % 360f
                val color = Color.hsv(hue, 0.8f, 1f)

                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = 4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    ),
                    alpha = 0.8f
                )
            }
        }
    }
}
