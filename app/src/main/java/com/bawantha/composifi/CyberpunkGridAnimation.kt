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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.pow

@Composable
fun CyberpunkGridAnimation(modifier: Modifier = Modifier) {
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
        val centerY = height * 0.4f // Horizon line

        // Draw sun
        val sunRadius = size.minDimension * 0.25f
        val sunGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFD700), Color(0xFFFF1493)),
            startY = centerY - sunRadius,
            endY = centerY + sunRadius
        )
        drawCircle(
            brush = sunGradient,
            radius = sunRadius,
            center = Offset(width / 2f, centerY)
        )

        // Draw grid
        val gridColor = Color(0xFF00FFFF) // Neon cyan
        val numVerticalLines = 15
        val numHorizontalLines = 10

        // Vertical perspective lines
        for (i in 0..numVerticalLines) {
            val progress = i.toFloat() / numVerticalLines
            val xTop = width * 0.2f + (width * 0.6f * progress)
            val xBottom = width * -1f + (width * 3f * progress)

            drawLine(
                color = gridColor,
                start = Offset(xTop, centerY),
                end = Offset(xBottom, height),
                strokeWidth = 2f,
                alpha = 0.6f
            )
        }

        // Horizontal moving lines
        val speed = 0.5f // Grid movement speed
        val offsetTime = (time * speed) % 1f

        for (i in 0..numHorizontalLines) {
            // Apply a power function to create perspective spacing (closer lines are further apart)
            val baseProgress = (i.toFloat() + offsetTime) / numHorizontalLines
            if (baseProgress > 1f) continue // Skip lines that fall off the bottom

            val perspectiveProgress = baseProgress.pow(2f)
            val y = centerY + (height - centerY) * perspectiveProgress

            // Calculate width of the line at this y position to clip it to the grid edges
            val yProgress = (y - centerY) / (height - centerY)
            // Left boundary
            val xStartTop = width * 0.2f
            val xStartBottom = width * -1f
            val xStart = xStartTop + (xStartBottom - xStartTop) * yProgress
            // Right boundary
            val xEndTop = width * 0.8f
            val xEndBottom = width * 2f
            val xEnd = xEndTop + (xEndBottom - xEndTop) * yProgress

            // Alpha fades out near the horizon
            val lineAlpha = (yProgress * 1.5f).coerceIn(0f, 0.8f)

            drawLine(
                color = gridColor,
                start = Offset(xStart, y),
                end = Offset(xEnd, y),
                strokeWidth = 1f + (yProgress * 3f),
                alpha = lineAlpha
            )
        }

        // Sun scanlines (to make it look like a retro 80s sun)
        val scanlinePath = Path()
        val numScanlines = 8
        for(i in 0 until numScanlines) {
             val scanY = centerY + sunRadius * 0.1f + (i * sunRadius * 0.15f)
             if(scanY > centerY + sunRadius) break

             // Calculate width of sun at this y
             val dy = scanY - centerY
             // Pythagoras to find x offset
             // x^2 + y^2 = r^2  => x = sqrt(r^2 - y^2)
             val dx = kotlin.math.sqrt((sunRadius * sunRadius) - (dy * dy).coerceAtLeast(0f))

             drawLine(
                 color = Color(0xFF121212), // Match typical dark background or use surface color, using a dark gray here
                 start = Offset(width/2f - dx, scanY),
                 end = Offset(width/2f + dx, scanY),
                 strokeWidth = (i + 1) * 1.5f
             )
        }
    }
}
