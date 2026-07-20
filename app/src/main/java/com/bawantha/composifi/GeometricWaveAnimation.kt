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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun GeometricWaveAnimation(modifier: Modifier = Modifier) {
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
        val centerY = height / 2f

        val points = 50
        val spacing = width / points

        for (i in 0 until points) {
            val x = i * spacing
            val yOffset = sin(i * 0.2f + time * 2f) * (height / 3f)
            val y = centerY + yOffset

            val color = Color.hsv((i * 10f + time * 100f) % 360f, 0.8f, 1f)

            drawCircle(
                color = color,
                radius = 8f + sin(i * 0.5f + time) * 4f,
                center = Offset(x, y),
                style = Stroke(width = 2f)
            )
        }
    }
}
