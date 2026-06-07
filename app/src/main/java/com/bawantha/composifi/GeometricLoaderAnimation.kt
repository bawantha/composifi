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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun GeometricLoaderAnimation(modifier: Modifier = Modifier) {
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
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxRadius = size.minDimension / 2.5f

        val numShapes = 4
        for (i in 0 until numShapes) {
            val phaseOffset = i * 0.5f
            val scale = 0.5f + 0.5f * sin(time * 3f + phaseOffset)
            val rotation = time * 50f * (if (i % 2 == 0) 1f else -1f)

            val color = Color.hsv((time * 50f + i * 40f) % 360f, 0.8f, 1f)

            rotate(degrees = rotation, pivot = Offset(centerX, centerY)) {
                val rectSize = maxRadius * 2f * scale
                drawRect(
                    color = color,
                    topLeft = Offset(centerX - rectSize / 2f, centerY - rectSize / 2f),
                    size = Size(rectSize, rectSize),
                    style = Stroke(width = 6f)
                )
            }
        }
    }
}
