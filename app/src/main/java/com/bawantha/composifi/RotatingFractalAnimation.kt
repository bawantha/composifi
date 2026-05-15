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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive

@Composable
fun RotatingFractalAnimation(modifier: Modifier = Modifier) {
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

        val maxRadius = size.minDimension / 2f * 0.9f
        val numLayers = 12

        for (i in 0 until numLayers) {
            val progress = i.toFloat() / numLayers
            val rotationAngle = time * 30f + (progress * 360f) + (time * i * 5f)

            // Pulsing scale factor based on time and layer index
            val scale = 1f - progress + (kotlin.math.sin(time * 2f + progress * Math.PI).toFloat() * 0.1f)
            val currentRadius = maxRadius * scale

            // Interpolate colors to create a rainbow effect
            val hue = (time * 50f + progress * 360f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            rotate(degrees = rotationAngle, pivot = Offset(centerX, centerY)) {
                drawRect(
                    color = color,
                    topLeft = Offset(centerX - currentRadius, centerY - currentRadius),
                    size = Size(currentRadius * 2, currentRadius * 2),
                    style = Stroke(
                        width = 4f * scale + 1f,
                        cap = StrokeCap.Round
                    ),
                    alpha = 0.8f
                )
            }
        }
    }
}
