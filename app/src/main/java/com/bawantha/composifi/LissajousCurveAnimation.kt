package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun LissajousCurveAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2

            // Parameters for the Lissajous curve
            val a = 5f
            val b = 4f
            val A = width / 2 * 0.9f
            val B = height / 2 * 0.9f
            val delta = PI / 2

            path.reset()

            // Number of segments to draw the trailing effect
            val segments = 200
            val maxHistory = 4f // Time window to draw

            for (i in 0..segments) {
                // Calculate time backwards from current time
                val tOffset = (i.toFloat() / segments) * maxHistory
                val t = time - tOffset

                // If t is negative, don't draw
                if (t < 0) continue

                val x = centerX + A * sin(a * t + delta).toFloat()
                val y = centerY + B * sin(b * t).toFloat()

                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            // Draw the curve with varying color
            val hue = (time * 50f) % 360f
            val strokeColor = Color.hsv(hue, 0.8f, 1f)

            drawPath(
                path = path,
                color = strokeColor,
                style = Stroke(
                    width = 4f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
