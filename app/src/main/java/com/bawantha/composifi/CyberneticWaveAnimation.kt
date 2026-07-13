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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CyberneticWaveAnimation(modifier: Modifier = Modifier) {
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

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2f

        val colors = listOf(
            Color(0xFF00FFFF), // Cyan
            Color(0xFFFF00FF), // Magenta
            Color(0xFFFFFF00)  // Yellow
        )

        val lines = 5
        val segments = 100

        for (i in 0 until lines) {
            path.reset()
            val phaseOffset = i * (PI.toFloat() / lines)
            val color = colors[i % colors.size]

            for (j in 0..segments) {
                val progress = j.toFloat() / segments
                val x = progress * width

                val yOffset = sin(progress * 10f + time * 2f + phaseOffset) * (height * 0.3f) +
                              cos(progress * 5f - time + phaseOffset) * (height * 0.1f)
                val y = centerY + yOffset

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 4f, cap = StrokeCap.Round),
                alpha = 0.7f
            )
        }
    }
}
