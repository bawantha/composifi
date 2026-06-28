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
import kotlin.math.sin

@Composable
fun RainbowWaveAnimation(modifier: Modifier = Modifier) {
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
        val numPoints = 100
        val numWaves = 5

        for (w in 0 until numWaves) {
            val hue = (w.toFloat() / numWaves * 360f + time * 50f) % 360f
            val color = Color.hsv(hue, 1f, 1f)

            for (i in 0 until numPoints) {
                val x = width * (i.toFloat() / numPoints)
                // y is a sine wave
                val frequency = 2f + w * 0.5f
                val amplitude = height * 0.2f
                val y = centerY + amplitude * sin(time * 3f + (x / width) * Math.PI.toFloat() * frequency)

                drawCircle(
                    color = color,
                    radius = 4f,
                    center = Offset(x, y)
                )
            }
        }
    }
}
