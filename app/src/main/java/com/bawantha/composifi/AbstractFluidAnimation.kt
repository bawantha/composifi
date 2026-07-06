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
fun AbstractFluidAnimation(modifier: Modifier = Modifier) {
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

        val maxRadius = size.minDimension * 0.45f

        val layerCount = 5
        val points = 60

        for (i in 0 until layerCount) {
            path.reset()
            val phase = i * (PI.toFloat() / 2f) + time

            val hue = (time * 20f + i * 30f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f, 0.7f)

            for (j in 0..points) {
                val angle = (j.toFloat() / points) * 2f * PI.toFloat()

                // create a wobbly, organic shape
                val r = maxRadius +
                        sin(angle * 3f + time * 1.5f + phase) * 20f +
                        cos(angle * 5f - time + phase) * 15f

                val x = centerX + r * cos(angle)
                val y = centerY + r * sin(angle)

                if (j == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 4f + i * 1.5f,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
