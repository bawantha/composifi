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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InternetCoolAnimationEffects(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val count = 20
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val maxRadius = size.minDimension / 2f * 0.9f

        for (i in 0 until count) {
            val t = time * 2f + i * 0.2f
            val radius = maxRadius * 0.5f + sin(t) * maxRadius * 0.3f
            val angle = time + i * (2 * PI / count).toFloat()

            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)

            val hue = (time * 100f + i * 18f) % 360f
            val color = Color.hsv(hue, 1f, 1f, 0.8f)

            drawCircle(
                color = color,
                radius = 10f,
                center = Offset(x, y),
                style = Stroke(width = 3f)
            )

            drawLine(
                color = color.copy(alpha = 0.5f),
                start = Offset(centerX, centerY),
                end = Offset(x, y),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }
    }
}
