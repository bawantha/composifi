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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SiriWaveAnimation(modifier: Modifier = Modifier) {
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
        val points = 200

        // Configuration for the wave components
        val waves = listOf(
            WaveConfig(color = Color(0x88FF295E), amplitude = 0.5f, frequency = 2f, phaseShift = 2f, speed = 2f),
            WaveConfig(color = Color(0x8829A4FF), amplitude = 0.8f, frequency = 3f, phaseShift = 4f, speed = 3f),
            WaveConfig(color = Color(0x885EFF29), amplitude = 0.3f, frequency = 4f, phaseShift = 6f, speed = 4f),
            WaveConfig(color = Color(0xFFFFFFFF), amplitude = 1.0f, frequency = 2.5f, phaseShift = 0f, speed = 2.5f)
        )

        val globalAmplitude = size.minDimension * 0.4f

        waves.forEachIndexed { index, config ->
            path.reset()
            for (j in 0..points) {
                // Normalize x to range [-1, 1]
                val normalizedX = (j.toFloat() / points) * 2f - 1f
                val x = (normalizedX + 1f) / 2f * width

                // Envelope function to taper the ends
                val envelope = 1f - normalizedX * normalizedX

                // Calculate y
                val waveY = sin(normalizedX * PI.toFloat() * config.frequency + time * config.speed + config.phaseShift)
                val y = centerY + globalAmplitude * config.amplitude * waveY * envelope

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = config.color,
                style = Stroke(
                    width = if (index == waves.size - 1) 4f else 3f,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

private data class WaveConfig(
    val color: Color,
    val amplitude: Float,
    val frequency: Float,
    val phaseShift: Float,
    val speed: Float
)
