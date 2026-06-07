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
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NewtonsCradleAnimation(modifier: Modifier = Modifier) {
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
        val count = 5
        val radius = size.minDimension * 0.08f
        val stringLength = size.minDimension * 0.6f
        val topY = size.height * 0.1f
        val startX = size.width / 2 - (count - 1) * radius

        val cycleTime = 0.5f
        val t = (time % (cycleTime * 2))

        val maxAngle = (PI / 3.5).toFloat()

        val leftAngle = if (t < cycleTime) {
            maxAngle * sin(t * PI.toFloat() / cycleTime)
        } else 0f

        val rightAngle = if (t >= cycleTime) {
            -maxAngle * sin((t - cycleTime) * PI.toFloat() / cycleTime)
        } else 0f

        // Draw top bar
        drawLine(
            color = Color.DarkGray,
            start = Offset(startX - radius * 2, topY),
            end = Offset(startX + (count - 1) * 2 * radius + radius * 2, topY),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )

        for (i in 0 until count) {
            val angle = when (i) {
                0 -> if (leftAngle > 0) leftAngle else 0f
                count - 1 -> if (rightAngle < 0) rightAngle else 0f
                else -> 0f
            }

            val anchorX = startX + i * 2 * radius
            val anchorY = topY

            val ballX = anchorX - stringLength * sin(angle)
            val ballY = anchorY + stringLength * cos(angle)

            // Draw string
            drawLine(
                color = Color.LightGray,
                start = Offset(anchorX, anchorY),
                end = Offset(ballX, ballY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Draw ball
            val ballGradient = Brush.radialGradient(
                colors = listOf(Color.White, Color.Cyan, Color.Blue),
                center = Offset(ballX - radius * 0.3f, ballY - radius * 0.3f),
                radius = radius * 1.5f
            )
            drawCircle(
                brush = ballGradient,
                radius = radius,
                center = Offset(ballX, ballY)
            )
        }
    }
}
