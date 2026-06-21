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
fun BubbleChatAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val bubbleCount = 12

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        for (i in 0 until bubbleCount) {
            val progress = (time * 0.5f + i.toFloat() / bubbleCount) % 1f
            val y = height - progress * height * 1.5f + height * 0.2f

            val xOffset = sin(time * 2f + i.toFloat()) * width * 0.2f
            val x = width * (0.2f + 0.6f * (i.toFloat() % 2f)) + xOffset

            val radius = size.minDimension * 0.05f + sin(time * 3f + i.toFloat()).toFloat() * size.minDimension * 0.02f

            val alpha = if (progress < 0.1f) {
                progress * 10f
            } else if (progress > 0.8f) {
                (1f - progress) * 5f
            } else {
                1f
            }

            val hue = (time * 20f + i * 30f) % 360f
            val color = Color.hsv(hue, 0.6f, 1f, alpha.coerceIn(0f, 1f))

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
