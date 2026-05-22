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
import kotlinx.coroutines.isActive

@Composable
fun SynthwaveGridAnimation(modifier: Modifier = Modifier) {
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

        // Draw the background sky (dark space)
        drawRect(
            color = Color(0xFF0F001A),
            size = size
        )

        // Draw the glowing sun
        val sunRadius = size.minDimension * 0.25f
        val sunCenter = Offset(width / 2f, height * 0.45f)

        // Sun gradient: yellow/orange to pink
        val sunBrush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFF007F)),
            startY = sunCenter.y - sunRadius,
            endY = sunCenter.y + sunRadius
        )

        drawCircle(
            brush = sunBrush,
            radius = sunRadius,
            center = sunCenter
        )

        // Draw "scanlines" over the sun for retro effect
        val numScanlines = 8
        for (i in 0 until numScanlines) {
            val yOffset = sunCenter.y + sunRadius * 0.2f + (i * (sunRadius * 0.8f / numScanlines))
            val sliceHeight = sunRadius * 0.05f * (i + 1) * 0.5f
            drawRect(
                color = Color(0xFF0F001A),
                topLeft = Offset(sunCenter.x - sunRadius, yOffset),
                size = androidx.compose.ui.geometry.Size(sunRadius * 2, sliceHeight)
            )
        }

        // Draw the grid (perspective)
        val horizonY = height * 0.55f

        // Horizon line
        drawLine(
            color = Color(0xFF00FFFF), // Cyan
            start = Offset(0f, horizonY),
            end = Offset(width, horizonY),
            strokeWidth = 2f
        )

        // Moving grid lines (horizontal)
        val gridSpeed = 2f
        val numHorizontalLines = 10
        val baseGridY = horizonY
        val gridHeight = height - horizonY

        // Animate the offset of horizontal lines moving forward
        val progress = (time * gridSpeed) % 1f

        for (i in 0 until numHorizontalLines + 1) {
            // Apply a perspective function (pow) to space lines closer at the horizon
            val lineProgress = (i.toFloat() + progress) / numHorizontalLines.toFloat()
            if (lineProgress <= 1f) {
                val perspectiveY = baseGridY + gridHeight * (lineProgress * lineProgress)

                // Fade out lines as they get closer to horizon
                val alpha = (lineProgress * 1.5f).coerceIn(0f, 1f)

                drawLine(
                    color = Color(0xFFFF007F).copy(alpha = alpha), // Pink
                    start = Offset(0f, perspectiveY),
                    end = Offset(width, perspectiveY),
                    strokeWidth = 1f + lineProgress * 2f
                )
            }
        }

        // Vertical lines (converging to center of horizon)
        val numVerticalLines = 15
        val centerX = width / 2f

        for (i in 0..numVerticalLines) {
            val startX = (i.toFloat() / numVerticalLines) * width

            // Calculate where this line hits the horizon based on perspective
            val horizonX = centerX + (startX - centerX) * 0.1f // converge factor

            drawLine(
                color = Color(0xFFFF007F).copy(alpha = 0.7f), // Pink
                start = Offset(horizonX, horizonY),
                end = Offset(startX, height),
                strokeWidth = 1.5f
            )
        }

        // Grid glow at the bottom
        val bottomGlowBrush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xFFFF007F).copy(alpha = 0.3f)),
            startY = horizonY + gridHeight * 0.5f,
            endY = height
        )
        drawRect(
            brush = bottomGlowBrush,
            topLeft = Offset(0f, horizonY),
            size = androidx.compose.ui.geometry.Size(width, gridHeight)
        )
    }
}
