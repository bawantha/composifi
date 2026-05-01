package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NeonInfinityLoopAnimation(modifier: Modifier = Modifier) {
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
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        val scale = size.minDimension / 3.5f

        path.reset()

        val numPoints = 200
        for (i in 0..numPoints) {
            val t = (i.toFloat() / numPoints) * 2 * Math.PI.toFloat()
            // Lemniscate of Bernoulli equations
            val x = (scale * 1.5f * cos(t)) / (1 + sin(t) * sin(t))
            val y = (scale * 1.5f * cos(t) * sin(t)) / (1 + sin(t) * sin(t))

            if (i == 0) {
                path.moveTo(centerX + x, centerY + y)
            } else {
                path.lineTo(centerX + x, centerY + y)
            }
        }

        // Draw glow (multiple semi-transparent strokes)
        for (i in 1..4) {
            drawPath(
                path = path,
                color = Color(0xFF00FFFF).copy(alpha = 0.2f / i),
                style = Stroke(
                    width = 4f * i * density,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // Draw main path
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(
                width = 2f * density,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw animated particles trailing along the path
        val numParticles = 3
        val speed = 1.5f // Controls the speed of particles

        for (i in 0 until numParticles) {
            // Offset particle position based on time and index
            val t = ((time * speed) + (i.toFloat() / numParticles) * 2 * Math.PI.toFloat()) % (2 * Math.PI.toFloat())

            val particleX = centerX + (scale * 1.5f * cos(t)) / (1 + sin(t) * sin(t))
            val particleY = centerY + (scale * 1.5f * cos(t) * sin(t)) / (1 + sin(t) * sin(t))

            drawCircle(
                color = Color.White,
                radius = 4f * density,
                center = Offset(particleX, particleY)
            )
            drawCircle(
                color = Color(0xFFFF00FF).copy(alpha = 0.6f),
                radius = 8f * density,
                center = Offset(particleX, particleY)
            )
        }
    }
}
