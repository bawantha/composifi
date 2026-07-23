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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun FibonacciSpiralAnimation(modifier: Modifier = Modifier) {
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

        val numPoints = 200
        val goldenAngle = 137.5f * (Math.PI / 180f).toFloat()

        for (i in 0 until numPoints) {
            val r = sqrt(i.toFloat()) * (size.minDimension * 0.03f)
            val theta = i * goldenAngle + time

            val x = centerX + r * cos(theta)
            val y = centerY + r * sin(theta)

            val hue = (i * 2f + time * 50f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            drawCircle(
                color = color,
                radius = size.minDimension * 0.015f,
                center = Offset(x, y)
            )
        }
    }
}
