package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InfinityIllusionAnimation(modifier: Modifier = Modifier) {
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
        val scale = size.minDimension * 0.45f

        val dots = 150

        for (i in 0 until dots) {
            val t = (i.toFloat() / dots) * 2f * PI.toFloat()

            // Lemniscate of Gerono
            val offsetT = t + time * 1.5f

            val x = centerX + scale * cos(offsetT)
            val y = centerY + scale * sin(2f * offsetT) * 0.5f

            // Add wave illusion
            val sizeWave = (sin(t * 6f - time * 12f) + 1f) * 0.5f

            val hue = (i.toFloat() / dots * 360f + time * 60f) % 360f

            drawCircle(
                color = Color.hsv(hue, 0.8f, 1f, alpha = 0.9f),
                radius = 2f + sizeWave * 12f,
                center = Offset(x, y)
            )
        }
    }
}
