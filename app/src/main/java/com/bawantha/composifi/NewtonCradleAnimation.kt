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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NewtonCradleAnimation(modifier: Modifier = Modifier) {
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
        val topY = height * 0.2f

        val ballRadius = size.minDimension * 0.08f
        val stringLength = height * 0.5f

        val numBalls = 5
        val spacing = ballRadius * 2
        val startX = centerX - (numBalls - 1) * spacing / 2f

        val period = 1.0f
        val phase = (time % period) / period
        val maxAngle = (PI / 4).toFloat() // 45 degrees

        for (i in 0 until numBalls) {
            val pivotX = startX + i * spacing
            val pivotY = topY

            var angle = 0f

            if (i == 0) {
                // Leftmost ball swings when phase is < 0.5
                if (phase < 0.5f) {
                    val swingPhase = phase * 2f
                    angle = maxAngle * sin(swingPhase * PI.toFloat())
                }
            } else if (i == numBalls - 1) {
                // Rightmost ball swings when phase is >= 0.5
                if (phase >= 0.5f) {
                    val swingPhase = (phase - 0.5f) * 2f
                    angle = -maxAngle * sin(swingPhase * PI.toFloat())
                }
            }

            val ballX = pivotX - stringLength * sin(angle)
            val ballY = pivotY + stringLength * cos(angle)

            // Draw string
            drawLine(
                color = Color.LightGray,
                start = Offset(pivotX, pivotY),
                end = Offset(ballX, ballY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Draw ball
            drawCircle(
                color = Color(0xFFCCCCCC),
                radius = ballRadius,
                center = Offset(ballX, ballY)
            )

            // Highlight for 3D effect
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = ballRadius * 0.3f,
                center = Offset(ballX - ballRadius * 0.3f, ballY - ballRadius * 0.3f)
            )
        }

        // Draw top bar
        drawLine(
            color = Color.DarkGray,
            start = Offset(startX - spacing, topY),
            end = Offset(startX + numBalls * spacing, topY),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
    }
}
