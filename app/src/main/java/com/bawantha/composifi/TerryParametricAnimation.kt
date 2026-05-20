package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun TerryParametricAnimation(modifier: Modifier = Modifier) {
    val timeState = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                timeState.floatValue = (frameTime - startTime) / 5_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val currentTime = timeState.floatValue
        val w = size.width
        val h = size.height
        val scaleW = size.minDimension
        val scaleH = size.minDimension

        val maxT = currentTime.toInt()
        val startT = maxOf(0, maxT - 800)

        for (t in startT..maxT step 2) {
            val tFloat = t.toFloat()
            val x = (sin(11 * PI * tFloat / 240) * sin(10 * PI * tFloat / 240) * (scaleW / 2.5) + (w / 2)).toFloat()
            val y = (sin(5 * PI * tFloat / 240) * (scaleH / 2.5) + (h / 2)).toFloat()

            val r = (200 + sin(tFloat / 120) * 55).toInt().coerceIn(0, 255)
            val g = Math.abs((sin(tFloat / 120) * 255).toInt()).coerceIn(0, 255)
            val b = (200 + sin(tFloat / 60) * 55).toInt().coerceIn(0, 255)

            val age = maxT - t
            val alpha = (1f - (age / 800f)).coerceIn(0f, 1f)

            val color = Color(
                red = r,
                green = g,
                blue = b,
                alpha = (alpha * 255).toInt()
            )

            val nextTFloat = (t + 2).toFloat()
            val nextX = (sin(11 * PI * nextTFloat / 240) * sin(10 * PI * nextTFloat / 240) * (scaleW / 2.5) + (w / 2)).toFloat()
            val nextY = (sin(5 * PI * nextTFloat / 240) * (scaleH / 2.5) + (h / 2)).toFloat()

            drawLine(
                color = color,
                start = Offset(x, y),
                end = Offset(nextX, nextY),
                strokeWidth = maxOf(2f, size.minDimension * 0.02f),
                cap = StrokeCap.Round
            )
        }
    }
}
