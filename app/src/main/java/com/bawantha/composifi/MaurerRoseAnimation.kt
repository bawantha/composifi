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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MaurerRoseAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }
    val rosePath = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size.minDimension * 0.45f

        // Animate n and d parameters based on time
        val n = 6f + sin(time * 0.2f) * 4f // Varies between 2 and 10
        val d = 71f + cos(time * 0.15f) * 30f // Varies between 41 and 101

        val color = Color(0xFF00FFCC) // Cyan color

        // Draw the Maurer rose
        path.reset()
        for (i in 0..360) {
            val k = i * d * (PI / 180f)
            val r = radius * sin(n * k)
            val x = centerX + r * cos(k)
            val y = centerY + r * sin(k)

            if (i == 0) {
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }

        drawPath(
            path = path,
            color = color.copy(alpha = 0.5f),
            style = Stroke(width = 2f)
        )

        // Draw the regular rose on top for contrast
        rosePath.reset()
        for (i in 0..360) {
            val k = i * (PI / 180f)
            val r = radius * sin(n * k)
            val x = centerX + r * cos(k)
            val y = centerY + r * sin(k)

            if (i == 0) {
                rosePath.moveTo(x.toFloat(), y.toFloat())
            } else {
                rosePath.lineTo(x.toFloat(), y.toFloat())
            }
        }

        drawPath(
            path = rosePath,
            color = Color(0xFFFF007F), // Neon Pink
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
    }
}
