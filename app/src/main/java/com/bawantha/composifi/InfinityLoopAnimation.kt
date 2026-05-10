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
fun InfinityLoopAnimation(modifier: Modifier = Modifier) {
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
        val centerX = width / 2f
        val centerY = height / 2f
        val a = size.minDimension * 0.45f

        val points = 200
        val colors = listOf(
            Color(0xFF00F0FF), // Neon Cyan
            Color(0xFFFF007F), // Neon Pink
            Color(0xFF7FFF00)  // Neon Green
        )

        for (i in colors.indices) {
            val color = colors[i]
            val phaseOffset = i * (PI / colors.size).toFloat()

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * 2f * PI.toFloat()

                // Lemniscate of Bernoulli
                val sinT = sin(t)
                val cosT = cos(t)
                val denominator = 1f + sinT * sinT

                val x = centerX + (a * cosT) / denominator
                val y = centerY + (a * sinT * cosT) / denominator

                // Apply some animated rotation or wave effect
                val waveOffset = sin(time * 3f + phaseOffset) * 10f
                val finalY = y + waveOffset * cos(t * 2f)

                if (j == 0) {
                    path.moveTo(x, finalY)
                } else {
                    path.lineTo(x, finalY)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 6f + (sin(time * 4f + phaseOffset).toFloat() + 1f) * 3f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.7f
            )

            // Draw a particle traversing the loop
            val particleT = (time * 0.5f + phaseOffset) % (2f * PI.toFloat())
            val sinPartT = sin(particleT)
            val cosPartT = cos(particleT)
            val partDenominator = 1f + sinPartT * sinPartT
            val partX = centerX + (a * cosPartT) / partDenominator
            val partY = centerY + (a * sinPartT * cosPartT) / partDenominator
            val wavePartOffset = sin(time * 3f + phaseOffset) * 10f
            val finalPartY = partY + wavePartOffset * cos(particleT * 2f)

            drawCircle(
                color = Color.White,
                radius = 8f,
                center = Offset(partX, finalPartY),
                alpha = 0.9f
            )
        }
    }
}
