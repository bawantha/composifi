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
fun SpirographAnimation(modifier: Modifier = Modifier) {
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
        val R = size.minDimension * 0.25f // fixed circle radius
        val r = size.minDimension * 0.1f // rolling circle radius
        val d = size.minDimension * 0.15f // distance from rolling circle center to drawing point

        val points = 200
        val colors = listOf(
            Color(0xFF00FFFF), // Cyan
            Color(0xFFFF00FF), // Magenta
            Color(0xFFFFFF00)  // Yellow
        )

        for (i in 0 until 3) {
            val color = colors[i % colors.size]
            val phaseOffset = i * (PI * 2 / 3).toFloat() + time * 0.5f

            path.reset()
            for (j in 0..points) {
                val t = (j.toFloat() / points) * 20f * PI.toFloat() // 10 revolutions

                val rotationT = t + time + phaseOffset

                val x = centerX + (R - r) * cos(rotationT) + d * cos(((R - r) / r) * rotationT)
                val y = centerY + (R - r) * sin(rotationT) - d * sin(((R - r) / r) * rotationT)

                if (j == 0) {
                    path.moveTo(x.toFloat(), y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 3f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.7f
            )
        }
    }
}
