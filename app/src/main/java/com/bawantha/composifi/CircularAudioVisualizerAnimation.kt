package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularAudioVisualizerAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = minOf(size.width, size.height) * 0.25f
        val numBars = 60
        val maxAmplitude = minOf(size.width, size.height) * 0.2f

        for (i in 0 until numBars) {
            val angle = (i.toFloat() / numBars) * Math.PI * 2

            val freq1 = sin(time * 5f + i * 0.5f)
            val freq2 = cos(time * 3f - i * 0.2f)
            val freq3 = sin(time * 8f + i * 0.8f)

            val amplitude = (freq1 + freq2 + freq3) / 3f
            val positiveAmplitude = (amplitude + 1f) / 2f

            val barLength = baseRadius + positiveAmplitude * maxAmplitude

            val startX = center.x + baseRadius * cos(angle).toFloat()
            val startY = center.y + baseRadius * sin(angle).toFloat()

            val endX = center.x + barLength * cos(angle).toFloat()
            val endY = center.y + barLength * sin(angle).toFloat()

            val hue = (i * 360f / numBars + time * 100f) % 360f

            drawLine(
                color = Color.hsv(hue, 0.8f, 1f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }

        val pulseRadius = baseRadius * 0.8f + sin(time * 4f) * baseRadius * 0.1f
        drawCircle(
            color = Color.hsv((time * 100f) % 360f, 0.8f, 0.5f),
            radius = pulseRadius,
            center = center
        )
    }
}
