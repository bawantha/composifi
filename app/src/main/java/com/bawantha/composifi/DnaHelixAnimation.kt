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
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DnaHelixAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f

        val points = 25
        val amplitude = size.minDimension * 0.35f
        val verticalSpacing = height / (points - 1)

        // Colors
        val color1 = Color(0xFF00F0FF) // Cyan
        val color2 = Color(0xFFFF007F) // Pink
        val lineColor = Color.Gray.copy(alpha = 0.5f)

        for (i in 0 until points) {
            val y = i * verticalSpacing

            // Calculate phase
            val phase = (i.toFloat() / points) * 4f * PI.toFloat() + time * 3f

            val xOffset1 = sin(phase).toFloat() * amplitude
            val x1 = centerX + xOffset1

            val xOffset2 = sin(phase + PI).toFloat() * amplitude
            val x2 = centerX + xOffset2

            // Determine Z index conceptually for depth sorting (scale/alpha based on cos(phase))
            val z1 = cos(phase).toFloat()
            val z2 = cos(phase + PI).toFloat()

            val radius1 = 6f + z1 * 3f
            val radius2 = 6f + z2 * 3f
            val alpha1 = 0.6f + (z1 + 1f) / 5f
            val alpha2 = 0.6f + (z2 + 1f) / 5f

            // Draw connecting line
            drawLine(
                color = lineColor,
                start = Offset(x1, y),
                end = Offset(x2, y),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )

            // Draw point 1 (back point first)
            if (z1 < z2) {
                drawCircle(color = color1, radius = radius1, center = Offset(x1, y), alpha = alpha1)
                drawCircle(color = color2, radius = radius2, center = Offset(x2, y), alpha = alpha2)
            } else {
                drawCircle(color = color2, radius = radius2, center = Offset(x2, y), alpha = alpha2)
                drawCircle(color = color1, radius = radius1, center = Offset(x1, y), alpha = alpha1)
            }
        }
    }
}
