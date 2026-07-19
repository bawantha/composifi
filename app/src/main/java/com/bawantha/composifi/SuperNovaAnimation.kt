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
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SuperNovaAnimation(modifier: Modifier = Modifier) {
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
        val center = Offset(width / 2f, height / 2f)
        val maxRadius = size.minDimension / 2f

        val numRays = 60
        for (i in 0 until numRays) {
            val angle = (i.toFloat() / numRays) * 2f * PI.toFloat()
            val wave = sin(time * 3f + i * 0.2f)
            val rayLength = maxRadius * 0.3f + maxRadius * 0.7f * (0.5f + 0.5f * wave)
            val rotation = time * 0.5f

            val finalAngle = angle + rotation
            val endPoint = Offset(
                x = center.x + rayLength * cos(finalAngle),
                y = center.y + rayLength * sin(finalAngle)
            )

            val hue = ((finalAngle * 180f / PI.toFloat() + time * 50f) % 360f + 360f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            drawLine(
                color = color,
                start = center,
                end = endPoint,
                strokeWidth = 3f + 2f * maxOf(0f, sin(time * 5f + i).toFloat()),
                cap = StrokeCap.Round,
                alpha = 0.7f + 0.3f * wave
            )
        }
    }
}
