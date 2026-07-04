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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InfinityLoopAnimation(modifier: Modifier = Modifier) {
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
        val cx = width / 2f
        val cy = height / 2f
        val a = size.minDimension * 0.4f

        val particleCount = 20

        for (i in 0 until particleCount) {
            val tOffset = time + (i * PI * 2 / particleCount).toFloat()
            val t = tOffset * 1.5f
            val scale = 2f / (3f - cos(2f * t))
            val x = scale * cos(t) * a
            val y = scale * sin(2f * t) / 2f * a

            val hue = (tOffset * 50f + i * (360f / particleCount)) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            drawCircle(
                color = color,
                radius = a * 0.08f,
                center = Offset(cx + x, cy + y)
            )
        }
    }
}
