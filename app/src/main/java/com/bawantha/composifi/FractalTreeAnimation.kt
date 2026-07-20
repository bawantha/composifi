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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FractalTreeAnimation(modifier: Modifier = Modifier) {
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

        val startX = width / 2f
        val startY = height

        val angleVariation = (sin(time) * 0.4).toFloat()

        fun drawBranch(x: Float, y: Float, len: Float, angle: Float, depth: Int) {
            if (depth == 0) return

            val endX = x + len * sin(angle)
            val endY = y - len * cos(angle)

            drawLine(
                color = Color.hsv((time * 30 + depth * 30) % 360f, 0.8f, 0.8f),
                start = Offset(x, y),
                end = Offset(endX, endY),
                strokeWidth = depth * 1f,
                cap = StrokeCap.Round
            )

            drawBranch(endX, endY, len * 0.7f, angle - 0.6f + angleVariation, depth - 1)
            drawBranch(endX, endY, len * 0.7f, angle + 0.6f - angleVariation, depth - 1)
        }

        drawBranch(startX, startY, height / 3.5f, 0f, 7)
    }
}
