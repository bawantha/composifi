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
import androidx.compose.ui.graphics.drawscope.Fill
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun LiquidWaveAnimation(modifier: Modifier = Modifier) {
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

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val waveAmplitude = size.minDimension * 0.1f
        val waveFrequency1 = (2.0 * Math.PI / width).toFloat()
        val waveFrequency2 = (2.0 * Math.PI / width * 0.8).toFloat()
        val baseHeight = height * 0.6f

        path1.reset()
        path1.moveTo(0f, height)
        path1.lineTo(0f, baseHeight)

        var x = 0f
        while (x <= width) {
            val y = baseHeight + waveAmplitude * sin((waveFrequency1 * x + time * 3f).toDouble()).toFloat()
            path1.lineTo(x, y)
            x += 5f
        }
        path1.lineTo(width, height)
        path1.close()

        drawPath(
            path = path1,
            color = Color(0xFF2196F3).copy(alpha = 0.5f),
            style = Fill
        )

        // Second wave
        path2.reset()
        path2.moveTo(0f, height)
        path2.lineTo(0f, baseHeight)

        x = 0f
        while (x <= width) {
            val y = baseHeight + waveAmplitude * 1.2f * sin((waveFrequency2 * x + time * 2f + 1f).toDouble()).toFloat()
            path2.lineTo(x, y)
            x += 5f
        }
        path2.lineTo(width, height)
        path2.close()

        drawPath(
            path = path2,
            color = Color(0xFF03A9F4).copy(alpha = 0.8f),
            style = Fill
        )
    }
}
