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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlinx.coroutines.isActive
import kotlin.math.pow

@Composable
fun RetroSynthwaveAnimation(modifier: Modifier = Modifier) {
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
        val horizonY = height * 0.5f

        // Draw sky background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E)),
                startY = 0f,
                endY = horizonY
            ),
            size = Size(width, horizonY)
        )

        // Draw sun with retro lines cutting through it
        val sunRadius = size.minDimension * 0.25f
        val sunCenterY = horizonY - sunRadius * 0.3f

        clipRect(top = 0f, bottom = horizonY) {
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF007F), Color(0xFFFFD700)),
                    startY = sunCenterY - sunRadius,
                    endY = sunCenterY + sunRadius
                ),
                radius = sunRadius,
                center = Offset(width / 2f, sunCenterY)
            )

            // Draw cutouts in the sun (retro style)
            val numCutouts = 6
            for (i in 0 until numCutouts) {
                val cutoutHeight = 4f * (i + 1)
                val cutoutY = sunCenterY + sunRadius - (i * 12f) - cutoutHeight
                drawRect(
                    color = Color(0xFF24243E), // Match sky background
                    topLeft = Offset(width / 2f - sunRadius, cutoutY),
                    size = Size(sunRadius * 2f, cutoutHeight)
                )
            }
        }

        // Draw ground background
        drawRect(
            color = Color(0xFF000000),
            topLeft = Offset(0f, horizonY),
            size = Size(width, height - horizonY)
        )

        // Draw 3D Perspective Grid
        val lines = 15
        val gridSpeed = 1f
        val offset = (time * gridSpeed) % 1f

        clipRect(top = horizonY, bottom = height) {
            // Horizontal lines moving forward
            for (i in 0..lines) {
                val progress = (i.toFloat() + offset) / lines
                // Quadratic mapping for perspective depth
                val yOffset = horizonY + (height - horizonY) * progress.pow(2.5f)

                drawLine(
                    color = Color(0xFF00FFFF).copy(alpha = progress * 0.8f + 0.2f),
                    start = Offset(0f, yOffset),
                    end = Offset(width, yOffset),
                    strokeWidth = 1f + 3f * progress
                )
            }

            // Vertical lines converging to a vanishing point
            val vanishingPointX = width / 2f
            for (i in -lines..lines) {
                val xPos = width / 2f + (i.toFloat() * width / 8f)

                drawLine(
                    color = Color(0xFF00FFFF).copy(alpha = 0.6f),
                    start = Offset(vanishingPointX, horizonY),
                    end = Offset(xPos * 4f - width * 1.5f, height),
                    strokeWidth = 2f
                )
            }
        }
    }
}
