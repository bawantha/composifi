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
import kotlin.math.sin

@Composable
fun NeonWaveAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path1 = remember { Path() }
    val path2 = remember { Path() }
    val path3 = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        path1.reset()
        path2.reset()
        path3.reset()

        val points = 100
        val stepX = width / points

        for (i in 0..points) {
            val x = i * stepX
            val t = (i.toFloat() / points) * 2f * PI.toFloat()

            val y1 = centerY + sin(t + time * 2f) * height * 0.3f
            val y2 = centerY + sin(t * 1.5f + time * 3f) * height * 0.2f
            val y3 = centerY + sin(t * 2f + time * 1.5f) * height * 0.4f

            if (i == 0) {
                path1.moveTo(x, y1.toFloat())
                path2.moveTo(x, y2.toFloat())
                path3.moveTo(x, y3.toFloat())
            } else {
                path1.lineTo(x, y1.toFloat())
                path2.lineTo(x, y2.toFloat())
                path3.lineTo(x, y3.toFloat())
            }
        }

        drawPath(
            path = path1,
            color = Color(0xFF00F0FF), // Neon Cyan
            style = Stroke(width = 6f, cap = StrokeCap.Round),
            alpha = 0.8f
        )

        drawPath(
            path = path2,
            color = Color(0xFFFF007F), // Neon Pink
            style = Stroke(width = 4f, cap = StrokeCap.Round),
            alpha = 0.8f
        )

        drawPath(
            path = path3,
            color = Color(0xFF7FFF00), // Neon Green
            style = Stroke(width = 5f, cap = StrokeCap.Round),
            alpha = 0.6f
        )
    }
}
