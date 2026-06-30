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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun AuroraAnimation(modifier: Modifier = Modifier) {
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

        val colors = listOf(
            Color(0xFF00FFCC),
            Color(0xFF3333FF),
            Color(0xFFFF00FF)
        )

        for (i in 0 until 3) {
            path.reset()
            val waveHeight = height * 0.4f
            val yOffset = height * 0.5f

            path.moveTo(0f, height)

            val stepSize = 10
            for (x in 0..width.toInt() step stepSize) {
                val xNorm = x / width
                val y = yOffset + waveHeight * sin(xNorm * 4f + time * (1f + i * 0.3f)) * sin(xNorm * 2f - time * (0.5f + i * 0.2f))
                if (x == 0) {
                    path.lineTo(0f, y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            path.lineTo(width, height)
            path.close()

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(colors[i].copy(alpha = 0.6f), Color.Transparent),
                    startY = yOffset - waveHeight,
                    endY = height
                ),
                style = Fill
            )
        }
    }
}
