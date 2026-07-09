package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun BouncingBallGridAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val ballRadius = size.minDimension * 0.1f
        val ballX = size.width / 2f + sin(time * 2f) * (size.width * 0.3f)
        val ballY = size.height / 2f + sin(time * 3f) * (size.height * 0.3f)
        val ballPos = Offset(ballX, ballY)

        val spacing = size.minDimension / 10f
        val rows = (size.height / spacing).toInt()
        val cols = (size.width / spacing).toInt()

        for (i in 0..rows) {
            for (j in 0..cols) {
                val x = j * spacing
                val y = i * spacing
                val cellCenter = Offset(x + spacing / 2, y + spacing / 2)

                val distance = (cellCenter - ballPos).getDistance()
                val influence = (1f - (distance / (size.minDimension * 0.4f))).coerceIn(0f, 1f)

                val scale = 1f - influence * 0.5f
                val cellSize = spacing * 0.8f * scale

                val hue = (time * 20f + (x + y) * 0.1f) % 360f
                val color = Color.hsv(hue, 0.7f, 1f)

                drawRect(
                    color = color,
                    topLeft = Offset(cellCenter.x - cellSize / 2, cellCenter.y - cellSize / 2),
                    size = Size(cellSize, cellSize)
                )
            }
        }

        // Draw the bouncing ball
        drawCircle(
            color = Color.White,
            radius = ballRadius,
            center = ballPos
        )
    }
}
