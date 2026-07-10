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
fun SineWaveArtAnimation(modifier: Modifier = Modifier) {
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
        val amplitude = height / 3f

        val numWaves = 5
        val colors = listOf(
            Color(0xFFff0055),
            Color(0xFF00ff99),
            Color(0xFF00ccff),
            Color(0xFFffcc00),
            Color(0xFFcc00ff)
        )

        for (i in 0 until numWaves) {
            path.reset()
            val phaseOffset = i * (PI.toFloat() / numWaves)
            val speed = 1f + (i * 0.2f)
            val waveLength = width / (1f + (i * 0.5f))

            var first = true
            for (x in 0..width.toInt() step 5) {
                val y = centerY + amplitude * sin((x.toFloat() / waveLength) * 2f * PI.toFloat() + time * speed + phaseOffset)

                if (first) {
                    path.moveTo(x.toFloat(), y)
                    first = false
                } else {
                    path.lineTo(x.toFloat(), y)
                }
            }

            drawPath(
                path = path,
                color = colors[i % colors.size],
                style = Stroke(
                    width = 4f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.7f
            )
        }
    }
}
