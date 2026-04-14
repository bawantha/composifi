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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun NeonRingAnimation(modifier: Modifier = Modifier) {
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
        val radiusBase = size.minDimension * 0.3f

        val rings = 5
        for (i in 0 until rings) {
            val offsetTime = time + i * 0.5f
            val pulse = (sin(offsetTime * 2f).toFloat() + 1f) / 2f // 0 to 1
            val radius = radiusBase + (pulse * radiusBase * 0.5f)

            val hue = (offsetTime * 50f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f, alpha = 1f - pulse * 0.5f)

            drawCircle(
                color = color,
                radius = radius,
                style = Stroke(width = 10f * (1f - pulse * 0.5f))
            )
        }
    }
}
