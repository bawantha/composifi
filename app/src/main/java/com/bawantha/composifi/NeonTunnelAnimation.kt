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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeonTunnelAnimation(modifier: Modifier = Modifier) {
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
        val maxRadius = size.minDimension

        val numShapes = 20
        val sides = 6 // Hexagon

        for (i in 0 until numShapes) {
            val progress = ((time * 0.5f) + (i.toFloat() / numShapes)) % 1f
            // Use an exponential scale to give a 3D depth effect
            val scale = Math.pow(progress.toDouble(), 3.0).toFloat()
            val alpha = if (progress < 0.2f) {
                progress / 0.2f
            } else if (progress > 0.8f) {
                (1f - progress) / 0.2f
            } else {
                1f
            }

            val currentRadius = maxRadius * scale
            val rotation = time * 30f + progress * 90f // Rotate as it comes closer

            path.reset()
            for (j in 0..sides) {
                val angle = (j * 2 * PI / sides)
                val x = currentRadius * cos(angle).toFloat()
                val y = currentRadius * sin(angle).toFloat()
                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            withTransform({
                translate(centerX, centerY)
                rotate(rotation)
            }) {
                val hue = (time * 100f + i * (360f / numShapes)) % 360f
                val color = Color.hsv(hue, 0.8f, 1f, alpha)
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = 4f + 8f * scale)
                )
            }
        }
    }
}
